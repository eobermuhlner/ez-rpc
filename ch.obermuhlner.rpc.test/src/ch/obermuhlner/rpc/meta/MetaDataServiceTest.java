package ch.obermuhlner.rpc.meta;

import java.io.File;

import org.junit.Test;

import ch.obermuhlner.rpc.exception.meta.RpcMetaDataException;

public class MetaDataServiceTest {

	@Test
	public void testMetaDataLoadSame() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice.xml"));
		}
		
	}

	@Test(expected = RpcMetaDataException.class)
	public void testMetaDataLoadDifferentServiceName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_service_name.xml"));
		}
	}

	@Test
	public void testMetaDataLoadDifferentServiceJavaName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_service_javaname.xml"));
		}
	}

	@Test(expected = RpcMetaDataException.class)
	public void testMetaDataLoadDifferentMethodName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_method_name.xml"));
		}
	}

	@Test
	public void testMetaDataLoadDifferentMethodJavaName() {
		try (MetaDataService metaDataService = new MetaDataService()) {
			metaDataService.load(new File("testservice.xml"));
			metaDataService.load(new File("testservice_different_method_javaname.xml"));
		}
	}
}
