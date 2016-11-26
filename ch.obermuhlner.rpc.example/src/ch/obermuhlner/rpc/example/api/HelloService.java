package ch.obermuhlner.rpc.example.api;

public interface HelloService {

	void ping();
	
	double calculateSquare(double value);
	
	ExampleData exampleMethod(ExampleData exampleData);
}
