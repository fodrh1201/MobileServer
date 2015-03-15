package proactor;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public class Dispatcher implements CompletionHandler <AsynchronousSocketChannel, AsynchronousServerSocketChannel>{

	private int DATA_SIZE = 6;
	private NioHandleMap handleMap;
	
	public Dispatcher(NioHandleMap handleMap) {
		this.handleMap = handleMap;
	}
	
	public void completed(AsynchronousSocketChannel channel,
			AsynchronousServerSocketChannel listener) {
		
		
		//버퍼를 만들고 비동기 콜백 방식으로 읽은 결과를 받아온다.
		ByteBuffer buffer = ByteBuffer.allocate(DATA_SIZE);
		channel.read(buffer, buffer, new Demultiplexer(handleMap, channel));
		
		listener.accept(listener, this);
		
	}

	public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
		
	}

}
