package ch.obermuhlner.rpc.example.server;

import ch.obermuhlner.rpc.example.api.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public void ping() {
		new RuntimeException("Ping").printStackTrace();
	}

	@Override
	public double calculateSquare(double value) {
		return value * value;
	}

}
