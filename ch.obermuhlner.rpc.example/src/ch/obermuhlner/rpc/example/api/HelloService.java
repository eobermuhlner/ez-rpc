package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcArgument;
import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcService;

@RpcService(name = "HelloService")
public interface HelloService {

	void ping();
	
	double calculateSquare(double value);
	
	@RpcMethod(name = "enrichExample")
	ExampleData exampleMethod(
			@RpcArgument(name = "poor")
			ExampleData exampleData);
}
