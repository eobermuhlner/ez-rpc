package ch.obermuhlner.rpc.example.client;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.LocalTransport;

public class HelloServiceRemoteLocalTransportApp {


	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		LocalTransport transport = new LocalTransport();
		
		ServiceFactory.publishService(HelloService.class, helloServiceImpl, transport);
		HelloService proxyService = ServiceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, transport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		
		helloServiceClient.runExample();
	}

}
