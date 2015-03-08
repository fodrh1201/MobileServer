import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Dispatcher {
	
	private final int HEADER_SIZE = 6;
	
	public void dispatch(ServerSocket serverSocket, HandleMap map) {
		try {
			Socket socket = serverSocket.accept();
			demultiplex(socket, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void demultiplex(Socket socket, HandleMap map) {
		try {
			InputStream inputStream = socket.getInputStream();
			
			byte[] buffer = new byte[HEADER_SIZE];
			inputStream.read(buffer);
			
			String header = new String(buffer);
			
			EventHandler handler = map.get(header);
			handler.handleEvent(inputStream);
			
			socket.close();
//			
//			switch (header) {
//			case "0x5001":
//				StreamSayHelloHandler sayHelloProtocol = new StreamSayHelloHandler();
//				sayHelloProtocol.handleEvent(inputStream);
//				break;
//			case "0x6001":
//				StreamUpdateProfileHandler updateProfileProtocol = new StreamUpdateProfileHandler();
//				updateProfileProtocol.handleEvent(inputStream);
//				break;
//			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
