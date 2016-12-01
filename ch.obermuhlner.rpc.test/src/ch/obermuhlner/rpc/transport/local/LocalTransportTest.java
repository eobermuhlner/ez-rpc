package ch.obermuhlner.rpc.transport.local;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class LocalTransportTest extends AbstractTransportTest {

	@BeforeClass
	public static void beforeClass() {
		MetaDataService metaDataService = new MetaDataService();
		
		StructureProtocol<Object> protocol = new BinaryProtocol<Object>(metaDataService, LocalTransportTest.class.getClassLoader());
		LocalTransport transport = new LocalTransport(metaDataService, protocol);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		TestServiceImpl testServiceImpl = new TestServiceImpl();

		serviceFactory.publishService(TestService.class, testServiceImpl, transport);
		
		testService = serviceFactory.createRemoteService(TestService.class, TestServiceAsync.class, transport);
		testServiceAsync = (TestServiceAsync) testService;
	}
	
	@AfterClass
	public static void afterClass() {
		testService = null;
	}
}
