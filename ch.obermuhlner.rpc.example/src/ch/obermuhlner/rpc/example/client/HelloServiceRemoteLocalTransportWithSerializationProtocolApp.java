package ch.obermuhlner.rpc.example.client;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.protocol.SerializableProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.LocalTransportWithProtocol;

public class HelloServiceRemoteLocalTransportWithSerializationProtocolApp {


	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		LocalTransportWithProtocol transport = new LocalTransportWithProtocol(new SerializableProtocol(HelloService.class.getClassLoader()));
		
		ServiceFactory.publishService(HelloService.class, helloServiceImpl, transport);
		HelloService proxyService = ServiceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, transport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		
		helloServiceClient.runExample();
	}

}
