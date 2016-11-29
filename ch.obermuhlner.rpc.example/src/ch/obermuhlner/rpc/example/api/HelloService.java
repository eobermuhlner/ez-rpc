package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcParameter;

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
