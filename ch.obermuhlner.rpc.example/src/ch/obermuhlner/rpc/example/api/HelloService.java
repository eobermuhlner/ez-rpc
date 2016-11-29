package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcParameter;
import ch.obermuhlner.rpc.annotation.RpcService;

@RpcService(name = "HelloService")
public interface HelloService {

	void ping();
	
	double calculateSquare(double value);
	
	@RpcMethod(name = "enrichExample")
	ExampleData exampleMethod(
			@RpcParameter(name = "poor")
			ExampleData exampleData);

	
	@RpcMethod(name = "adapterExample")
	AdapterExampleData adapterExampleMethod(
			@RpcParameter(name = "data")
			AdapterExampleData adapterExampleData);
	
	void exampleFailure() throws IllegalArgumentException;
}
