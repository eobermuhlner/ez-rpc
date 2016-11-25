package ch.obermuhlner.rpc.example.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.HelloServiceAsync;
import ch.obermuhlner.rpc.example.server.HelloServiceImpl;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.SerializableProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.SocketClientTransport;
import ch.obermuhlner.rpc.transport.SocketServerTransport;

public class HelloServiceRemoteSocketApp {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		HelloServiceClient helloServiceClient = new HelloServiceClient();
	
		HelloServiceImpl helloServiceImpl = new HelloServiceImpl();
		
		int port = 5924;
		Protocol protocol = new SerializableProtocol(HelloServiceImpl.class.getClassLoader());
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, "localhost", port);
		SocketServerTransport socketServerTransport = new SocketServerTransport(protocol, port);
		
		executorService.execute(() -> socketServerTransport.run());
		
		ServiceFactory.publishService(HelloService.class, helloServiceImpl, socketServerTransport);
		HelloService proxyService = ServiceFactory.createRemoteService(HelloService.class, HelloServiceAsync.class, socketClientTransport);
		
		helloServiceClient.setHelloService(proxyService);
		helloServiceClient.setHelloServiceAsync((HelloServiceAsync) proxyService);
		
		helloServiceClient.runExample();
		
		executorService.shutdownNow();
	}

}
