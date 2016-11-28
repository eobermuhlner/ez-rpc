package ch.obermuhlner.rpc.example.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
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
		exampleData.booleanField = true;
		exampleData.intField = 1;
		exampleData.longField = 1000;
		exampleData.stringField = "Hello";
		exampleData.listField = new ArrayList<>();
		exampleData.listField.add("alpha");
		exampleData.listField.add("beta");
		exampleData.setField = new HashSet<>();
		exampleData.setField.add("one");
		exampleData.setField.add("two");
		exampleData.mapField = new HashMap<>();
		exampleData.mapField.put(1, "one");
		exampleData.mapField.put(2, "two");
		exampleData.nestedExampleData = new ExampleData();
		System.out.println(helloService.exampleMethod(exampleData));
		
		AdapterExampleData adapterExampleData = new AdapterExampleData();
		adapterExampleData.bigDecimalField = BigDecimal.valueOf(1.234);
		adapterExampleData.dateField = new Date();
		adapterExampleData.localDateTimeField = LocalDateTime.now();
		adapterExampleData.localDateField = LocalDate.now();
		adapterExampleData.periodField = Period.ofDays(7);
		System.out.println(helloService.adapterExampleMethod(adapterExampleData));
	}
}
