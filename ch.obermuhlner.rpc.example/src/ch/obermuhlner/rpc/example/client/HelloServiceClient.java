package ch.obermuhlner.rpc.example.client;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;

public class HelloServiceClient {

	private HelloService helloService;
	private HelloServiceAsync helloServiceAsync;
	
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
	
	public void setHelloServiceAsync(HelloServiceAsync helloServiceAsync) {
		this.helloServiceAsync = helloServiceAsync;
	}
	
	public void runExample() {
		helloService.ping();
		
		System.out.println("square(3) = " + helloService.calculateSquare(3));
		
		CompletableFuture<Double> calculateSquareAsync = helloServiceAsync.calculateSquareAsync(4);
		calculateSquareAsync.thenAccept((result) -> {
			System.out.println("async square(4) = " + result);	
		});
	}
}
