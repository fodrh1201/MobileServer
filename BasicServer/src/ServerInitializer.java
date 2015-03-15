
public class ServerInitializer {
	public static void main(String[] args) {
//		reactor.ServerInitializer reactorServer = new reactor.ServerInitializer();
//		reactorServer.serverStart();

		proactor.ServerInitializer proactorServer = new proactor.ServerInitializer();
		proactorServer.serverStart();
		
		
	}
}
