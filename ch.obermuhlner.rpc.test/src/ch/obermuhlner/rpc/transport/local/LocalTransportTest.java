package ch.obermuhlner.rpc.transport.local;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;
import ch.obermuhlner.rpc.service.ServiceFactory;
import ch.obermuhlner.rpc.transport.AbstractTransportTest;

public class LocalTransportTest extends AbstractTransportTest {

	protected TestService getTestService() {
		MetaDataService metaDataService = new MetaDataService();
		metaDataService.registerService(TestService.class);
		
		Protocol<Object> protocol = new JavaSerializableProtocol(LocalTransportTest.class.getClassLoader());
		LocalTransport transport = new LocalTransport(metaDataService, protocol);
		ServiceFactory serviceFactory = new ServiceFactory(metaDataService);

		serviceFactory.publishService(TestService.class, new TestServiceImpl(), transport);
		TestService testService = serviceFactory.createRemoteService(TestService.class, transport);
		
		return testService;
	}
}
