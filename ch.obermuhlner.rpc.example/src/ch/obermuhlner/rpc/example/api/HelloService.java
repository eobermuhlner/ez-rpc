package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcService;

@RpcService(name = "HelloService")
public interface HelloService {

	void ping();
	
	double calculateSquare(double value);
	
	ExampleData exampleMethod(ExampleData exampleData);
}
