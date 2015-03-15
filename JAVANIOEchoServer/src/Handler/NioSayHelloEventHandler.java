package Handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class NioSayHelloEventHandler implements EventHandler {

	private static final int TOKEN_NUM = 2;
	
	private AsynchronousSocketChannel channel;
	//private ByteBuffer buffer;
	
	public void completed(Integer result, ByteBuffer buffer) {
		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			buffer.flip();
			
			String msg = new String(buffer.array());
			
			String[] params = new String[TOKEN_NUM];
			StringTokenizer token = new StringTokenizer(msg, "|");
			int  i = 0;
			while (token.hasMoreElements()) {
				params[i] = token.nextToken();
				i++;
			}
			
			sayHello(params);
			
			try {
				buffer.clear();
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sayHello(String[] params) {
		System.out.println("SayHello -> name : " + params[0] + " age : " + params[1]);
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		
	}

	public String getHandler() {
		// TODO Auto-generated method stub
		return "0x5001";
	}

	public int getDataSize() {
		// TODO Auto-generated method stub
		return 512;
	}

	public void handleEvent(InputStream inputStream) {
		// TODO Auto-generated method stub
		
	}

	public void initialize(AsynchronousSocketChannel cahnnel, ByteBuffer buffer) {
		// TODO Auto-generated method stub
		this.channel = cahnnel;
		//this.buffer = buffer;
	}

}
