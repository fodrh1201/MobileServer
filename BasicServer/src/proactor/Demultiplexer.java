package proactor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {

	private NioHandleMap handleMap;
	private AsynchronousSocketChannel channel;
	
	public Demultiplexer(NioHandleMap handleMap, AsynchronousSocketChannel channel) {
		this.handleMap = handleMap;
		this.channel = channel;
	}

	public void completed(Integer result, ByteBuffer buffer) {
		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip();
			
			String header = new String(buffer.array());
			NioEventHandler handler = handleMap.get(header);
			ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
			handler.initialize(channel, newBuffer);
			channel.read(newBuffer, newBuffer, handler);
		}
		
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		
	}

}
