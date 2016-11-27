package ch.obermuhlner.rpc.example.client.socket;

import java.io.File;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.client.HelloServiceClient;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ProtocolFactory;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketClientTransport;

public class HelloServiceSocketClientApp {

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = setupHelloServiceClient();
		
		helloServiceClient.runExample();
	}

	private static HelloServiceClient setupHelloServiceClient() {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		MetaDataService serviceMetaData = new MetaDataService();
		serviceMetaData.load(new File("rpc-metadata.xml"));
		serviceMetaData.registerService(HelloService.class);
		serviceMetaData.save(new File("rpc-metadata.xml"));
		
		int port = 5924;
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(serviceMetaData, HelloServiceImpl.class.getClassLoader());
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, "localhost", port);
		
		ServiceFactory serviceFactory = new ServiceFactory();
		HelloService proxyService = serviceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, socketClientTransport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		return helloServiceClient;
	}

}
