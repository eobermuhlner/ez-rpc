package ch.obermuhlner.rpc.example.app.socket;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ProtocolFactory;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketServerTransport;

public class HelloGenServiceSocketServerApp {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		int port = 5924;
		
		MetaDataService metaDataService = new MetaDataService(new File("rpc-metadata.xml"));
		
		StructureProtocol<Object> protocol = ProtocolFactory.binaryProtocol(metaDataService, HelloServiceImpl.class.getClassLoader());
		SocketServerTransport socketServerTransport = new SocketServerTransport(metaDataService, protocol, port);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);
		
		serviceFactory.publishService(HelloService.class, helloServiceImpl, socketServerTransport);

		System.out.println("Server running ...");
		executorService.execute(() -> socketServerTransport.run());
	}
}
