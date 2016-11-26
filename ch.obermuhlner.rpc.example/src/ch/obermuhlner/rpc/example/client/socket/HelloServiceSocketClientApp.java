package ch.obermuhlner.rpc.example.client.socket;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.client.HelloServiceClient;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureReader;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureWriter;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketClientTransport;

public class HelloServiceSocketClientApp {

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = setupHelloServiceClient();
		
		helloServiceClient.runExample();
	}

	private static HelloServiceClient setupHelloServiceClient() {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		int port = 5924;
		//Protocol<Object> protocol = new SerializableProtocol(HelloServiceImpl.class.getClassLoader());
		Protocol<Object> protocol = new StructureProtocol<Object>(
				(in) -> new BinaryStructureReader(in),
				(out) -> new BinaryStructureWriter(out),
				HelloServiceImpl.class.getClassLoader());
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, "localhost", port);
		
		HelloService proxyService = ServiceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, socketClientTransport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		return helloServiceClient;
	}

}
