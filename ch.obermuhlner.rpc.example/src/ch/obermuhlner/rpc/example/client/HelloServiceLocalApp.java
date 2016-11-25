package ch.obermuhlner.rpc.example.client;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.service.ServiceFactory;

public class HelloServiceLocalApp {


	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		HelloService proxyService = ServiceFactory.createLocalService(HelloService.class, HelloServiceAsync.class, helloServiceImpl);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		
		helloServiceClient.runExample();
	}

}
