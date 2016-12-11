package ch.obermuhlner.rpc.transport.local;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class DirectLocalTransportTest extends AbstractTransportTest {

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		DirectLocalTransport transport = new DirectLocalTransport(metaDataService);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		serviceFactory.publishService(TestService.class, testServiceImpl, transport);
		
		testService = serviceFactory.createRemoteService(TestService.class, TestServiceAsync.class, transport);
		testServiceAsync = (TestServiceAsync) testService;
	}
	
	@AfterClass
	public static void afterClass() {
		testService = null;
		testServiceAsync = null;
	}
}
