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
	
	public void registerHandler(String header, EventHandler handler) {
		map.put(header, handler);
	}
	
	public void removeHandler(EventHandler handler) {
		map.remove(handler.getHandler());
	}
}
