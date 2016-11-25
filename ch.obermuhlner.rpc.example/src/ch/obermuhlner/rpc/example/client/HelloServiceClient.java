package ch.obermuhlner.rpc.example.client;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;

public class HelloServiceClient {

	private HelloService helloService;
	
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
	
	public void runExample() {
		helloService.ping();
		
		System.out.println("square(3) = " + helloService.calculateSquare(3));
	}

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		helloServiceClient.setHelloService(new HelloServiceImpl());
		
		helloServiceClient.runExample();
	}
}
