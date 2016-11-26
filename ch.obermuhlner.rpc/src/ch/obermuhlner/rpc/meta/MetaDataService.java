package ch.obermuhlner.rpc.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcStruct;

public class MetaDataService {

	private final MetaData metaData = new MetaData();
	
	private final Map<String, StructDefinition> mapNameToStructDefinition = new HashMap<>();
	private final Map<String, StructDefinition> mapTypeToStructDefinition = new HashMap<>();

	public synchronized void load(File file) {
		if (!file.exists()) {
			return;
		}
		
		MetaData loading = loadMetaData(file);
		
		for (StructDefinition structDefinition : loading.getStructDefinitions()) {
			registerStruct(structDefinition);
		}
	}

	public synchronized void save(File file) {
		saveMetaData(metaData, file);
	}

	public synchronized StructDefinition registerStruct(Class<?> type) {
		String name = type.getName();
		
		RpcStruct annotation = type.getAnnotation(RpcStruct.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				name = annotation.name();
			}
		}
		
		return registerStruct(type, name);
	}
	
	public synchronized StructDefinition registerStruct(Class<?> type, String name) {
		StructDefinition structDefinition = new StructDefinition(name, type.getName());
		registerStruct(structDefinition);
		return structDefinition;
	}

	private void registerStruct(StructDefinition structDefinition) {
		metaData.addStructDefinition(structDefinition);

		mapNameToStructDefinition.put(structDefinition.name, structDefinition);
		mapTypeToStructDefinition.put(structDefinition.javaTypeName, structDefinition);
	}
	
	public StructDefinition getStructDefinition(String name) {
		return mapNameToStructDefinition.get(name);
	}

	public synchronized StructDefinition getStructDefinition(Class<?> type) {
		if (!mapTypeToStructDefinition.containsKey(type.getName())) {
			registerStruct(type);
		}
		
		return mapTypeToStructDefinition.get(type.getName());
	}

	public synchronized StructDefinition getStructDefinition(String name, ClassLoader classLoader) {
		StructDefinition structDefinition = getStructDefinition(name);
		if (structDefinition == null) {
			try {
				Class<?> type = Class.forName(name, false, classLoader);
				structDefinition = registerStruct(type, name);
			} catch (ClassNotFoundException e) {
				throw new RpcServiceException(e);
			}
		}
		
		return structDefinition;
	}

	public static void saveMetaData(MetaData metaData, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(MetaData.class);
			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(metaData, file);
		} catch (JAXBException e) {
			throw new RpcServiceException(e);
		}
	}
	
	public static MetaData loadMetaData(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(MetaData.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			return (MetaData) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			throw new RpcServiceException(e);
		}
	}
}
