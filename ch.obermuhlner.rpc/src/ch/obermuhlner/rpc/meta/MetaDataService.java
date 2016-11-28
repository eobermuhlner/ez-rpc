package ch.obermuhlner.rpc.meta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcMethod;
import ch.obermuhlner.rpc.annotation.RpcParameter;
import ch.obermuhlner.rpc.annotation.RpcService;
import ch.obermuhlner.rpc.annotation.RpcStruct;
import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class MetaDataService implements AutoCloseable {

	private File metaDataFile;

	private final MetaData metaData = new MetaData();
	
	private final List<Adapter<?, ?>> adapters = new ArrayList<Adapter<?, ?>>();

	public MetaDataService() {
		this(null);
	}

	public MetaDataService(File metaDataFile) {
		this.metaDataFile = metaDataFile;
		
		if (metaDataFile != null) {
			load(metaDataFile);
		}
	}
	
	@Override
	public void close() {
		if (metaDataFile != null) {
			save(metaDataFile);
		}
	}
	
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
	
	public void addAdapter(Adapter<?, ?> adapter) {
		adapters.add(adapter);
		
		registerStruct(adapter.getRemoteType());
	}

	public synchronized ServiceDefinition registerService(Class<?> type) {
		String name = type.getName();
		Class<?> sessionJavaClass = null;

		ServiceDefinition serviceDefinition = findServiceDefinitionByType(name);
		if (serviceDefinition != null) {
			return serviceDefinition;
		}

		RpcService annotation = type.getAnnotation(RpcService.class);
		if (annotation != null) {
			if (annotation.name() != null && !annotation.name().equals("")) {
				name = annotation.name();
			}
			if (annotation.session() != null && annotation.session() != Void.class) {
				sessionJavaClass = annotation.session();
			}
		}
		
		serviceDefinition = new ServiceDefinition(name, type.getName(), toTypeString(sessionJavaClass));

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
		
		methodDefinition.returns= toTypeString(method.getReturnType());
		
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

		parameterDefinition.type = toTypeString(parameter.getType());
		
		return parameterDefinition;
	}

	private void fillStructureDefinition(StructDefinition structDefinition, Class<?> type) {
		for (Field field : type.getFields()) {
			String fieldName = field.getName();
			String fieldType = toTypeString(field.getType());
			String elementType = toTypeString(field.getType().getComponentType());
			String keyType = null;
			String valueType = null;
			
			RpcField annotation = field.getAnnotation(RpcField.class);
			if (annotation != null) {
				if (annotation.element() != null && annotation.element() != Void.class) {
					elementType = toTypeString(annotation.element());
				}
				if (annotation.key() != null && annotation.key() != Void.class) {
					keyType = toTypeString(annotation.key());
				}
				if (annotation.value() != null && annotation.value() != Void.class) {
					valueType = toTypeString(annotation.value());
				}
			}
			
//			String neededType = null;
//			if (fieldType.equals(Type.LIST.toTypeName()) && elementType == null) {
//				neededType = "element";
//			} else if (fieldType.equals(Type.SET.toTypeName()) && elementType == null) {
//				neededType = "element";
//			} else if (fieldType.equals(Type.MAP.toTypeName()) && keyType == null) {
//				neededType = "key";
//			} else if (fieldType.equals(Type.MAP.toTypeName()) && valueType == null) {
//				neededType = "value";
//			}
//			
//			if (neededType != null) {
//				throw new RpcServiceException("Field '" + type.getName() + "." + field.getName() + "' of type '" + fieldType + "' must specify '" + neededType + "' type in @RpcField");
//			}

			FieldDefinition fieldDefinition = new FieldDefinition(fieldName, fieldType, elementType, keyType, valueType);
			structDefinition.fieldDefinitions.add(fieldDefinition);
		}
	}

	private Type toType(Class<?> type) {
		if (type == null) {
			return null;
		}
		if (type == Boolean.class || type == boolean.class) {
			return Type.BOOL;
		}
		if (type == Integer.class || type == int.class) {
			return Type.INT;
		}
		if (type == Long.class || type == long.class) {
			return Type.LONG;
		}
		if (type == Double.class || type == double.class) {
			return Type.DOUBLE;
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
	
	public String toTypeString(Class<?> javaClass) {
		@SuppressWarnings("rawtypes")
		Adapter adapter = findAdapterByLocalType(javaClass);
		if (adapter != null) {
			javaClass = adapter.getRemoteType();
		}

		Type type = toType(javaClass);
		if (type == null) {
			return null;
		}
		if (type == Type.STRUCT) {
			return registerStruct(javaClass).name;
		} else {
			return type.toTypeName();
		}
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
	
	public Class<?> getClass(Class<?> type, FieldDefinition fieldDefinition) {
		try {
			return type.getField(fieldDefinition.name).getType();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RpcServiceException(e);
		}
	}

	public Adapter<?, ?> findAdapterByLocalType(Class<?> localType) {
		return adapters.stream()
			.filter(adapter -> adapter.getLocalType() == localType)
			.findFirst()
			.orElse(null);
	}
	
	public Adapter<?, ?> findAdapterByRemoteType(Class<?> remoteType) {
		return adapters.stream()
			.filter(adapter -> adapter.getRemoteType() == remoteType)
			.findFirst()
			.orElse(null);
	}
	
	public FieldDefinition findFieldDefinition(String remoteTypeName, String fieldName) {
		StructDefinition structDefinition = findStructDefinitionByType(remoteTypeName);
		if (structDefinition == null) {
			return null;
		}
		return structDefinition.findFieldDefinition(fieldName);
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
