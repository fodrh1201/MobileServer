package Handler;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public interface EventHandler extends CompletionHandler<Integer, ByteBuffer>{

	public String getHandler();
	
	public int getDataSize();
	
	public void handleEvent(InputStream inputStream);
	
	public void initialize(AsynchronousSocketChannel cahnnel, ByteBuffer buffer);
}
