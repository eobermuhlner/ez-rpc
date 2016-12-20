package ch.obermuhlner.rpc.example.app.local;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.client.HelloServiceClient;
import ch.obermuhlner.rpc.service.ServiceFactory;
import jni.JniHelloService;

public class HelloServiceLocalJniApp {

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = setupHelloServiceClient();
		
		helloServiceClient.runExample();
	}

	private static HelloServiceClient setupHelloServiceClient() {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloService helloServiceImpl = new JniHelloService();
		
		ServiceFactory serviceFactory = new ServiceFactory(null);
		HelloService proxyService = serviceFactory.createLocalService(HelloService.class, HelloServiceAsync.class, helloServiceImpl);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		
		return helloServiceClient;
	}

}
