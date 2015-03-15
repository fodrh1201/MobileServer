package reactor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPoolDispatcher implements Dispatcher {
	static final String NUMTHREADS = "8";
	static final String THREADPROP = "Thread";

	private int numThreads;

	public ThreadPoolDispatcher() {
		numThreads = Integer.parseInt(System
				.getProperty(THREADPROP, NUMTHREADS));
	}

	@Override
	public void dispatch(final ServerSocket serverSocket, final HandleMap map) {
		for (int i = 0; i < numThreads - 1; i++) {
			Thread thread = new Thread() {
				public void run() {
					dispatchLoop(serverSocket, map);
				}
			};
			thread.start();
			System.out
					.println("created and start thread = " + thread.getName());

		}

		System.out.println("Iterative Server starting in main Thread "
				+ Thread.currentThread().getName());

		dispatchLoop(serverSocket, map);
	}

	private void dispatchLoop(ServerSocket serverSocket, HandleMap map) {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Demultiplexer demultiplexer = new Demultiplexer(socket, map);

				demultiplexer.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
