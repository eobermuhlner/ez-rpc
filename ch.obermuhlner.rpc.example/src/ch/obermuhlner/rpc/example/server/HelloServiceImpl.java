package ch.obermuhlner.rpc.example.server;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
import ch.obermuhlner.rpc.example.api.ExampleData;
import ch.obermuhlner.rpc.example.api.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public void ping() {
		new RuntimeException("Ping").printStackTrace(System.out);
	}

	@Override
	public double calculateSquare(double value) {
		return value * value;
	}

	@Override
	public ExampleData exampleMethod(ExampleData exampleData) {
		return exampleData;
	}

	@Override
	public AdapterExampleData adapterExampleMethod(AdapterExampleData adapterExampleData) {
		return adapterExampleData;
	}

	@Override
	public void exampleFailure() throws IllegalArgumentException {
		throw new IllegalArgumentException("Example for an exception");
	}
}
