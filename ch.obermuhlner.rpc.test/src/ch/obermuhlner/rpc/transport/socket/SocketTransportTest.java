package ch.obermuhlner.rpc.transport.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class SocketTransportTest extends AbstractTransportTest {

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		int port = 15924;
		String hostname = "localhost";
		
		StructureProtocol<Object> protocol = new BinaryProtocol<Object>(metaDataService, SocketTransportTest.class.getClassLoader());
		SocketServerTransport socketServerTransport = new SocketServerTransport(metaDataService, protocol, port);
		executorService.execute(() -> socketServerTransport.run());
		
		SocketClientTransport socketClientTransport = new SocketClientTransport(protocol, hostname, port);
		
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		serviceFactory.publishService(TestService.class, testServiceImpl, socketServerTransport);
		testService = serviceFactory.createRemoteService(TestService.class, TestServiceAsync.class, socketClientTransport);
		testServiceAsync = (TestServiceAsync) testService;
	}

	@AfterClass
	public static void afterClass() {
		testService = null;
		executorService.shutdown();
	}
}
