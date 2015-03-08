import java.io.IOException;
import java.net.ServerSocket;


public class Reactor {
	private HandleMap map;
	private ServerSocket serverSocket;
	
	public Reactor(int port) {
		map = new HandleMap();
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
		Dispatcher dispatcher = new Dispatcher();
		
		while (true) {
			dispatcher.dispatch(serverSocket, map);
		}
	}
	
	public void registerHandler(EventHandler handler) {
		map.put(handler.getHandler(), handler);
	}
}
