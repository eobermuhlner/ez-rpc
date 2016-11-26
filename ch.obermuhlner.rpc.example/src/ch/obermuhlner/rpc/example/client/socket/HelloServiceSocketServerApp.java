package ch.obermuhlner.rpc.example.client.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureReader;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureWriter;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketServerTransport;

public class HelloServiceSocketServerApp {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		int port = 5924;
		//Protocol<Object> protocol = new SerializableProtocol(HelloServiceImpl.class.getClassLoader());
		StructureProtocol<Object> protocol = new StructureProtocol<Object>(
				(in) -> new BinaryStructureReader(in),
				(out) -> new BinaryStructureWriter(out),
				HelloServiceImpl.class.getClassLoader());
		
		SocketServerTransport socketServerTransport = new SocketServerTransport(protocol, port);

		ServiceFactory.publishService(HelloService.class, helloServiceImpl, socketServerTransport);

		System.out.println("Server running ...");
		executorService.execute(() -> socketServerTransport.run());
	}

}
