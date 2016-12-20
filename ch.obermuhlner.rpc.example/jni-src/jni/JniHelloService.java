package jni;

import ch.obermuhlner.rpc.example.api.AdapterExampleData;
import ch.obermuhlner.rpc.example.api.ExampleData;
import ch.obermuhlner.rpc.example.api.HelloService;

public class JniHelloService implements HelloService {

	static {
		System.loadLibrary("JniHelloService");
	}
	
	@Override
	public native void ping();

	@Override
	public native double calculateSquare(double value);

	@Override
	public native ExampleData exampleMethod(ExampleData exampleData);

	@Override
	public native AdapterExampleData adapterExampleMethod(AdapterExampleData adapterExampleData);

	@Override
	public native void exampleFailure() throws IllegalArgumentException;

	public static void main(String[] args) {
		JniHelloService jniHelloService = new JniHelloService();
		
		jniHelloService.ping();
		
		System.out.println(jniHelloService.calculateSquare(3));
		
		ExampleData exampleData = new ExampleData();
		System.out.println(jniHelloService.exampleMethod(exampleData));
		
		AdapterExampleData adapterExampleData = new AdapterExampleData();
		System.out.println(jniHelloService.adapterExampleMethod(adapterExampleData));
		
		jniHelloService.exampleFailure();
	}
}
