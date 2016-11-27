package ch.obermuhlner.rpc.meta;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcStruct;

public class MetaDataService {

	private final MetaData metaData = new MetaData();
	
	public synchronized void load(File file) {
		if (!file.exists()) {
			return;
		}
		
		MetaData loading = loadMetaData(file);
		
		for (StructDefinition structDefinition : loading.getStructDefinitions().get()) {
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
	}
	
	public synchronized StructDefinition getStructDefinition(Class<?> type) {
		StructDefinition structDefinition = findStructDefinitionByType(type.getName());
		if (structDefinition != null) {
			return structDefinition;
		}
		
		return registerStruct(type);
	}

	public synchronized StructDefinition getStructDefinition(String name, ClassLoader classLoader) {
		StructDefinition structDefinition = findStructDefinitionByName(name);
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

	public StructDefinition findStructDefinitionByName(String name) {
		return metaData.getStructDefinitions().findByName(name);
	}
	
	public StructDefinition findStructDefinitionByType(String javaTypeName) {
		return metaData.getStructDefinitions().findByName(javaTypeName);
	}
	
	public StructDefinition findStructDefinitionMatching(StructDefinition template) {
		return metaData.getStructDefinitions().findByTemplate(template);
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
