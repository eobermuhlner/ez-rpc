package ch.obermuhlner.rpc.example.client.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ProtocolFactory;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketServerTransport;

public class HelloServiceSocketServerApp {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		MetaDataService serviceMetaData = new MetaDataService();

		int port = 5924;
		//Protocol<Object> protocol = new SerializableProtocol(HelloServiceImpl.class.getClassLoader());
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(serviceMetaData, HelloServiceImpl.class.getClassLoader());
		
		SocketServerTransport socketServerTransport = new SocketServerTransport(protocol, port);

		ServiceFactory serviceFactory = new ServiceFactory();
		serviceFactory.publishService(HelloService.class, helloServiceImpl, socketServerTransport);

		System.out.println("Server running ...");
		executorService.execute(() -> socketServerTransport.run());
	}

}
