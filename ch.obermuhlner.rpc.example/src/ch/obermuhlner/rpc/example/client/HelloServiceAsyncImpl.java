package ch.obermuhlner.rpc.example.client;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;

public class HelloServiceAsyncImpl implements HelloServiceAsync {

	private HelloService helloService;
	
	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
	
	@Override
	public CompletableFuture<Double> calculateSquareAsync(double value) {
		return CompletableFuture.supplyAsync(() -> {
			return helloService.calculateSquare(value);
		});
	}

}
