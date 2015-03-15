package Handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.StringTokenizer;

public class NioUpdateProfileHandler implements EventHandler{

private static final int TOKEN_NUM = 5;
	
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
			
			update(params);
			
			try {
				buffer.clear();
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void update(String[] params) {
		System.out.println("UpdateProfile -> " +
				" id :" + params[0] +
				" password : " + params[1] +
				" name : " + params[2] +
				" age : " + params[3] +
				" gender : " + params[4]);
		
	}

	
	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		
	}

	public String getHandler() {
		// TODO Auto-generated method stub
		return "0x6001";
	}

	public int getDataSize() {
		// TODO Auto-generated method stub
		return 1024;
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
