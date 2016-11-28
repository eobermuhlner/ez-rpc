package ch.obermuhlner.rpc.example.app.meta;

import java.io.File;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.meta.adapter.BigDecimalAdapter;
import ch.obermuhlner.rpc.meta.adapter.DateAdapter;

public class HelloMetaData {

	public static MetaDataService createMetaDataService() {
		MetaDataService metaDataService = new MetaDataService();
		metaDataService.addAdapter(new BigDecimalAdapter());
		metaDataService.addAdapter(new DateAdapter());

		metaDataService.load(new File("rpc-metadata.xml"));
		metaDataService.registerService(HelloService.class);
		metaDataService.save(new File("rpc-metadata.xml"));
		
		return metaDataService;
	}
}
