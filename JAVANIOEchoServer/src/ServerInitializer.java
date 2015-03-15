import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Handler.EventHandler;
import Handler.NioSayHelloEventHandler;
import Handler.NioUpdateProfileHandler;


public class ServerInitializer {
	private static int PORT = 5000;
	private static int threadPoolSize = 8;
	private static int initialSize = 4;
	private static int backlog = 50; 
	private static HandleMap handleMap;
	
	public static void main(String[] args) {
		
		System.out.println("Server Start");
		handleMap = new HandleMap();
		
		EventHandler sayHelloHandler = new NioSayHelloEventHandler();
		EventHandler updateHandler = new NioUpdateProfileHandler();
		
		handleMap.put(sayHelloHandler.getHandler(), sayHelloHandler);
		handleMap.put(updateHandler.getHandler(), updateHandler);
		
		// 고정 스레드 풀 생성 threadPoolSize 만큼의 스레드만 사용한다.
		ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
		
		/*
		 * 간단하게 : 사용자 지정 스레드 풀을 만들 수 있는 편리한 방법 제공.
		 */
		
		//캐시 스레드 풀 생성 - 초기에 스레드를 만들고 새 스레드는 필요한 만큼 생성한다. 이전에 만든 스레드를 이할 수 있다면 재사용할 수도 있다.
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executor, initialSize);
			/*
			 * AsynchronousChannelGroup 클래스
			 * 비동기 채널 그룹 개념을 위한, 채널을 위한
			 * 모든 스레드가 공유하는 리소스와 스레드풀을 캡슐화 함.
			 * 채널을 실제적으로 그룹이 소유하고 있고, 채널을 닫으면 그룹도 닫힘.
			 */
			
			//스트림 지향의 리스닝 소켓을 위한 비동기 채널
			AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
			listener.bind(new InetSocketAddress(PORT), backlog);
			
			//접속의 결과를 CompletionHandler으로 비동기 IO작업에 콜백 형식으로 작업 결과를 받는다.
			listener.accept(listener, new Dispatcher(handleMap));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
