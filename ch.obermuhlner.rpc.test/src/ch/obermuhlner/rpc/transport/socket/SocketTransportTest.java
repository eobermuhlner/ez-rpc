package ch.obermuhlner.rpc.transport.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class SocketTransportTest extends AbstractTransportTest {

	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	private static ServiceFactory serviceFactory;

	private static TestService testService;

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		int port = 15924;
		String hostname = "localhost";
		
		Protocol<Object> protocol = new JavaSerializableProtocol(SocketTransportTest.class.getClassLoader());
		SocketServerTransport socketServerTransport = new SocketServerTransport(metaDataService, protocol, port);
		executorService.execute(() -> socketServerTransport.run());
		
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, hostname, port);
		
		serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		serviceFactory.publishService(TestService.class, testServiceImpl, socketServerTransport);
		testService = serviceFactory.createRemoteService(TestService.class, socketClientTransport);
	}

	@AfterClass
	public static void afterClass() {
		testService = null;
		serviceFactory = null;
		executorService.shutdown();
	}
	
	protected TestService getTestService() {
		return testService;
	}
}
