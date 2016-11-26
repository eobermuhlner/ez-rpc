package ch.obermuhlner.rpc.service;

import java.util.HashMap;
import java.util.Map;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcStruct;

public class ServiceMetaData {

	private final Map<String, StructDefinition> mapNameToStructDefinition = new HashMap<>();
	private final Map<Class<?>, StructDefinition> mapTypeToStructDefinition = new HashMap<>();

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
		StructDefinition structDefinition = new StructDefinition(name, type);
		
		mapNameToStructDefinition.put(name, structDefinition);
		mapTypeToStructDefinition.put(type, structDefinition);
		
		return structDefinition;
	}
	
	public StructDefinition getStructDefinition(String name) {
		return mapNameToStructDefinition.get(name);
	}

	public synchronized StructDefinition getStructDefinition(Class<?> type) {
		if (!mapTypeToStructDefinition.containsKey(type)) {
			registerStruct(type);
		}
		
		return mapTypeToStructDefinition.get(type);
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
}
