package ch.obermuhlner.rpc.example.app.direct;

import ch.obermuhlner.rpc.example.client.HelloServiceAsyncImpl;
import ch.obermuhlner.rpc.example.client.HelloServiceClient;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;

public class HelloServiceDirectApp {

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
