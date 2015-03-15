package reactor;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class Demultiplexer implements Runnable {

	private static final int HEADER_SIZE = 6;
	private Socket socket;
	private HandleMap map;
	
	public Demultiplexer(Socket socket, HandleMap map) {
		this.socket = socket;
		this.map = map;
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			
			byte[] buffer = new byte[HEADER_SIZE];
			inputStream.read(buffer);
			
			String header = new String(buffer);
			
			EventHandler handler = map.get(header);
			handler.handleEvent(inputStream);
			
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
