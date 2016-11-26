package ch.obermuhlner.rpc.example.client;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.ExampleData;
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
		
		ExampleData exampleData = new ExampleData();
		exampleData.intField = 1;
		exampleData.longField = 1000;
		exampleData.stringField = "Hello";
		exampleData.listField = new ArrayList<>();
		exampleData.listField.add("alpha");
		exampleData.listField.add("beta");
		exampleData.nestedExampleData = new ExampleData();
		System.out.println(helloService.exampleMethod(exampleData));
	}
}
