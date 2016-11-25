package ch.obermuhlner.rpc.example.client;

import ch.obermuhlner.rpc.example.server.HelloServiceImpl;

public class HelloServiceSimpleLocalApp {


	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloService = new HelloServiceImpl();
		HelloServiceAsyncImpl helloServiceAsync = new HelloServiceAsyncImpl();
		helloServiceAsync.setHelloService(helloService);
		
		helloServiceClient.setHelloService(helloService);
		helloServiceClient.setHelloServiceAsync(helloServiceAsync);
		
		helloServiceClient.runExample();
	}

}
