package ch.obermuhlner.rpc.transport.local;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class LocalTransportTest extends AbstractTransportTest {

	private static TestService testService;

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		Protocol<Object> protocol = new JavaSerializableProtocol(LocalTransportTest.class.getClassLoader());
		LocalTransport transport = new LocalTransport(metaDataService, protocol);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		serviceFactory.publishService(TestService.class, testServiceImpl, transport);
		testService = serviceFactory.createRemoteService(TestService.class, transport);
	}
	
	@AfterClass
	public static void afterClass() {
		testService = null;
	}
	
	protected TestService getTestService() {
		return testService;
	}

}
