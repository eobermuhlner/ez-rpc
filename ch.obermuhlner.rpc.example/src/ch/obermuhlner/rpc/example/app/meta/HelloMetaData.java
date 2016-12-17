package ch.obermuhlner.rpc.example.app.meta;

import java.io.File;

import ch.obermuhlner.rpc.example.api.HelloService;
import ch.obermuhlner.rpc.example.api.Planet;
import ch.obermuhlner.rpc.meta.MetaDataService;

public class HelloMetaData {

	public static MetaDataService createMetaDataService() {
		try (MetaDataService metaDataService = new MetaDataService(new File("rpc-metadata.xml"))) {
			
			metaDataService.registerService(HelloService.class);
			metaDataService.registerEnum(Planet.class);
			
			return metaDataService;
		}
	}
}
