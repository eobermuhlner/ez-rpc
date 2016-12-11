package ch.obermuhlner.rpc.local;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class LocalTest extends AbstractTransportTest {

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		testService = serviceFactory.createLocalService(TestService.class, TestServiceAsync.class, testServiceImpl);
		testServiceAsync = (TestServiceAsync) testService;
	}
	
	@AfterClass
	public static void afterClass() {
		testService = null;
		testServiceAsync = null;
	}
}
