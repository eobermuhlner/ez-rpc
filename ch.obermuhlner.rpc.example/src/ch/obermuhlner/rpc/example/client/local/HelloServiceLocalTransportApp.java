package ch.obermuhlner.rpc.example.client.local;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.client.HelloServiceClient;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ProtocolFactory;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.LocalTransport;

public class HelloServiceLocalTransportApp {

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = setupHelloServiceClient();
		
		helloServiceClient.runExample();
	}

	private static HelloServiceClient setupHelloServiceClient() {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		MetaDataService serviceMetaData = new MetaDataService();
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(serviceMetaData, HelloServiceImpl.class.getClassLoader());

		LocalTransport transport = new LocalTransport(protocol);
		
		ServiceFactory serviceFactory = new ServiceFactory();
		serviceFactory.publishService(HelloService.class, helloServiceImpl, transport);
		HelloService proxyService = serviceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, transport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		return helloServiceClient;
	}

}
