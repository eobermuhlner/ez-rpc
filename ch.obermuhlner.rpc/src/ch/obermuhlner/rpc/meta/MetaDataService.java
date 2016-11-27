package ch.obermuhlner.rpc.meta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcParameter;
import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcService;
import ch.obermuhlner.rpc.annotation.RpcStruct;

public class MetaDataService {

	private final MetaData metaData = new MetaData();
	
	public synchronized void load(File file) {
		if (!file.exists()) {
			return;
		}
		
		MetaData loading = loadMetaData(file);
		
		for (StructDefinition structDefinition : loading.getStructDefinitions().get()) {
			metaData.addStructDefinition(structDefinition);
		}
	}

	public synchronized void save(File file) {
		saveMetaData(metaData, file);
	}

	public synchronized ServiceDefinition registerService(Class<?> type) {
		String name = type.getName();

		ServiceDefinition serviceDefinition = findServiceDefinitionByType(name);
		if (serviceDefinition != null) {
			return serviceDefinition;
		}

		RpcService annotation = type.getAnnotation(RpcService.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				name = annotation.name();
			}
		}
		
		serviceDefinition = new ServiceDefinition(name, type.getName());

		fillServiceDefinition(serviceDefinition, type);

		metaData.addServiceDefinition(serviceDefinition);

		return serviceDefinition;
	}
	
	public synchronized StructDefinition registerStruct(Class<?> type) {
		String name = type.getName();

		StructDefinition structDefinition = findStructDefinitionByType(name);
		if (structDefinition != null) {
			return structDefinition;
		}

		RpcStruct annotation = type.getAnnotation(RpcStruct.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				name = annotation.name();
			}
		}
		
		structDefinition = new StructDefinition(name, type.getName());

		metaData.addStructDefinition(structDefinition); // HACK - register incomplete to avoid recursive registration if type references itself
		fillStructureDefinition(structDefinition, type);

		metaData.addStructDefinition(structDefinition);

		return structDefinition;
	}
	
	private void fillServiceDefinition(ServiceDefinition serviceDefinition, Class<?> type) {
		for (Method method : type.getMethods()) {
			MethodDefinition methodDefinition = toMethodDefinition(method);
			serviceDefinition.methodDefinitions.add(methodDefinition);
		}
	}
	
	private MethodDefinition toMethodDefinition(Method method) {
		MethodDefinition methodDefinition = new MethodDefinition();
		
		methodDefinition.name = method.getName();
		RpcMethod annotation = method.getAnnotation(RpcMethod.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				methodDefinition.name = annotation.name();
			}			
		}
		
		methodDefinition.returnType = toType(method.getReturnType());
		if (methodDefinition.returnType == Type.STRUCT) {
			StructDefinition referencedStructDefinition = registerStruct(method.getReturnType());
			methodDefinition.returnStructName = referencedStructDefinition.name;
		}
		
		for (Parameter parameter : method.getParameters()) {
			ParameterDefinition parameterDefinition = toParameterDefinition(parameter);
			methodDefinition.parameterDefinitions.add(parameterDefinition);
		}
		
		return methodDefinition;
	}

	private ParameterDefinition toParameterDefinition(Parameter parameter) {
		ParameterDefinition parameterDefinition = new ParameterDefinition();
		
		parameterDefinition.name = parameter.getName();
		RpcParameter annotation = parameter.getAnnotation(RpcParameter.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				parameterDefinition.name = annotation.name();
			}			
		}

		parameterDefinition.type = toType(parameter.getType());
		if (parameterDefinition.type == Type.STRUCT) {
			StructDefinition referencedStructDefinition = registerStruct(parameter.getType());
			parameterDefinition.structName = referencedStructDefinition.name;
		}
		
		return parameterDefinition;
	}

	private void fillStructureDefinition(StructDefinition structDefinition, Class<?> type) {
		for (Field field : type.getFields()) {
			Type fieldType = toType(field.getType());
			if (type != null) {
				String structName = null;
				
				if (fieldType == Type.STRUCT) {
					StructDefinition referencedStructDefinition = registerStruct(field.getType());
					structName = referencedStructDefinition.name;
				}

				FieldDefinition fieldDefinition = new FieldDefinition(field.getName(), fieldType, structName);
				System.out.println(fieldDefinition);
				structDefinition.fieldDefinitions.add(fieldDefinition);
			}
		}
	}

	private Type toType(Class<?> type) {
		if (type == Boolean.class || type == boolean.class) {
			return Type.BOOL;
		}
		if (type == Integer.class || type == int.class) {
			return Type.INT;
		}
		if (type == Long.class || type == long.class) {
			return Type.LONG;
		}
		if (type == String.class) {
			return Type.STRING;
		}
		if (List.class.isAssignableFrom(type) || type == Object[].class) {
			return Type.LIST;
		}
		if (Set.class.isAssignableFrom(type)) {
			return Type.SET;
		}
		if (Map.class.isAssignableFrom(type)) {
			return Type.MAP;
		}

		if (type.getAnnotation(RpcStruct.class) != null) {
			return Type.STRUCT;
		}

		return null;
	}

	public synchronized StructDefinition getStructDefinition(String name, ClassLoader classLoader) {
		StructDefinition structDefinition = findStructDefinitionByName(name);
		if (structDefinition == null) {
			try {
				Class<?> type = Class.forName(name, false, classLoader);
				structDefinition = registerStruct(type);
			} catch (ClassNotFoundException e) {
				throw new RpcServiceException(e);
			}
		}
		
		return structDefinition;
	}

	private ServiceDefinition findServiceDefinitionByType(String javaTypeName) {
		return metaData.getServiceDefinitions().findByType(javaTypeName);
	}
	
	private StructDefinition findStructDefinitionByName(String name) {
		return metaData.getStructDefinitions().findByName(name);
	}
	
	private StructDefinition findStructDefinitionByType(String javaTypeName) {
		return metaData.getStructDefinitions().findByType(javaTypeName);
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
