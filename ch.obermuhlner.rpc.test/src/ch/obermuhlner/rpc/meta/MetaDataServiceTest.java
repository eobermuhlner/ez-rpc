package ch.obermuhlner.rpc.meta;

import java.io.File;

import org.junit.Test;

import ch.obermuhlner.rpc.exception.RpcException;

public class MetaDataServiceTest {

	@Test
	public void testMetaDataLoadSame() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice.xml"));
		}
		
	}

	@Test(expected = RpcException.class)
	public void testMetaDataLoadDifferentServiceName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_service_name.xml"));
		}
	}

	@Test(expected = RpcException.class)
	public void testMetaDataLoadDifferentServiceJavaName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_service_javaname.xml"));
		}
	}

	@Test(expected = RpcException.class)
	public void testMetaDataLoadDifferentMethodName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_method_name.xml"));
		}
	}
}
