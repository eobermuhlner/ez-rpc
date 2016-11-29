package ch.obermuhlner.rpc.protocol.structure;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.annotation.RpcStruct;
import ch.obermuhlner.rpc.data.DynamicStruct;
import ch.obermuhlner.rpc.meta.FieldDefinition;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.meta.StructDefinition;
import ch.obermuhlner.rpc.meta.adapter.Adapter;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class StructureProtocol<T> implements Protocol<T> {

	private final Function<InputStream, StructureReader> readerProvider;
	private final Function<OutputStream, StructureWriter> writerProvider;
	private final ClassLoader classLoader;
	private MetaDataService metaDataService;
	
	public StructureProtocol(MetaDataService metaDataService, Function<InputStream, StructureReader> readerProvider, Function<OutputStream, StructureWriter> writerProvider, ClassLoader classLoader) {
		this.metaDataService = metaDataService;
		this.readerProvider = readerProvider;
		this.writerProvider = writerProvider;
		this.classLoader = classLoader;
		
		metaDataService.registerStruct(Request.class);
		metaDataService.registerStruct(Response.class);
	}
	
	@Override
	public void serialize(OutputStream out, T element) {
		StructureWriter writer = writerProvider.apply(out);
		
		write(writer, element);
	}

	private void write(StructureWriter writer, Object element) {
		if (element == null) {
			writer.writeNull();
		} else if (element.getClass().isArray()) {
			int length = Array.getLength(element);
			writer.writeListBegin(length);
			for (int i = 0; i < length; i++) {
				write(writer, Array.get(element, i));
			}
			writer.writeListEnd();
		} else if (element instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) element;
			writer.writeListBegin(list.size());
			for (Object object : list) {
				write(writer, object);
			}
			writer.writeListEnd();
		} else if (element instanceof Set) {
			@SuppressWarnings("unchecked")
			Set<Object> set = (Set<Object>) element;
			writer.writeSetBegin(set.size());
			for (Object object : set) {
				write(writer, object);
			}
			writer.writeSetEnd();
		} else if (element instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) element;
			writer.writeMapBegin(map.size());
			for (Entry<Object, Object> entry : map.entrySet()) {
				writer.writeMapEntryBegin();
				write(writer, entry.getKey());
				write(writer, entry.getValue());
				writer.writeMapEntryEnd();
			}
			writer.writeSetEnd();
		} else if (element instanceof Boolean) {
			writer.writeBoolean((Boolean) element);
		} else if (element instanceof Integer) {
			writer.writeInt((Integer) element);
		} else if (element instanceof Long) {
			writer.writeLong((Long) element);
		} else if (element instanceof Double) {
			writer.writeDouble((Double) element);
		} else if (element instanceof String) {
			writer.writeString((String) element);
		} else {
			if (element instanceof DynamicStruct) {
				writeDynamicStruct(writer, (DynamicStruct) element);
			} else {
				if (element.getClass().getAnnotation(RpcStruct.class) != null) {
					writeStruct(writer, element);
				} else {
					throw new RpcServiceException("Class not marked as @RpcStruct and no matching Adapter found: " + element.getClass().getName());
				}
			}
		}
		
	}

	private void writeStruct(StructureWriter writer, Object element) {
		Class<?> type = element.getClass();

		writer.writeStructBegin(getStructName(type));
		
		for (Field field : type.getFields()) {
			field.setAccessible(true);
			FieldData fieldData = new FieldData();
			fieldData.name = field.getName();
			try {
				Object value = field.get(element); 
				FieldDefinition fieldDefinition = metaDataService.findFieldDefinition(type.getName(), fieldData.name);
				if (fieldDefinition == null) {
					throw new RpcServiceException("Unknown field: " + type.getName() + "." + fieldData.name);
				}
				Class<?> fieldType = metaDataService.getClass(type, fieldDefinition);
				value = convertToRemote(value, fieldType);
				fieldData.value = value;

				writeField(writer, fieldData);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RpcServiceException(e);
			}
		}
		
		writer.writeFieldStop();
		writer.writeStructEnd();
	}

	private void writeDynamicStruct(StructureWriter writer, DynamicStruct element) {
		writer.writeStructBegin(element.name);

		for (String fieldName : element.getFields()) {
			FieldData fieldData = new FieldData();
			fieldData.name = fieldName;
			fieldData.value = element.getField(fieldName);
			
			writeField(writer, fieldData);
		}
		
		writer.writeFieldStop();
		writer.writeStructEnd();
	}

	private void writeField(StructureWriter writer, FieldData fieldData) {
		writer.writeFieldBegin(fieldData.name);
		write(writer, fieldData.value);
		writer.writeFieldEnd();
	}

	private String getStructName(Class<?> type) {
		return metaDataService.registerStruct(type).name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(InputStream in) {
		StructureReader reader = readerProvider.apply(in);
		
		return (T) read(reader);
	}

	private Object read(StructureReader reader) {
		StructureType type = reader.readType();
		
		switch (type) {
		case NULL:
			return null;
		case DOUBLE:
			return reader.readDouble();
		case BOOL:
			return reader.readBoolean();
		case INT:
			return reader.readInt();
		case LONG:
			return reader.readLong();
		case STRING:
			return reader.readString();
		case LIST:
			return readList(reader);
		case MAP:
			return readMap(reader);
		case SET:
			return readSet(reader);
		case STRUCT:
			return readStruct(reader);
		case FIELD:
			throw new RpcServiceException("FIELD only allowed inside STRUCT");
		case FIELD_STOP:
			throw new RpcServiceException("FIELD_STOP only allowed inside STRUCT");
		}

		throw new RpcServiceException("Unkown type: " + type);
	}

	private List<Object> readList(StructureReader reader) {
		List<Object> list = new ArrayList<>();
		
		int size = reader.readListBegin();
		for (int i = 0; i < size; i++) {
			list.add(read(reader));
		}
		
		return list;
	}

	private Set<Object> readSet(StructureReader reader) {
		Set<Object> set = new HashSet<>();
		
		int size = reader.readSetBegin();
		for (int i = 0; i < size; i++) {
			set.add(read(reader));
		}
		
		return set;
	}

	private Map<Object, Object> readMap(StructureReader reader) {
		Map<Object, Object> set = new HashMap<>();
		
		int size = reader.readSetBegin();
		for (int i = 0; i < size; i++) {
			reader.readMapEntryBegin();
			set.put(read(reader), read(reader));
			reader.readMapEntryEnd();
		}
		
		return set;
	}
	
	private Object readStruct(StructureReader reader) {
		String name = reader.readStructBegin();

		Object struct = createStruct(name);
		StructureType type = reader.readType();
		while (type != StructureType.FIELD_STOP) {
			if (type != StructureType.FIELD) {
				throw new RpcServiceException("Type must be FIELD or FIELD_STOP: " + type);
			}
			
			FieldData field = readField(reader);
			addField(struct, field);
			
			type = reader.readType();
		}
		
		reader.readStructEnd();
		
		return struct;
	}

	private Object createStruct(String name) {
		StructDefinition structDefinition = metaDataService.getStructDefinition(name, classLoader);
		
		if (structDefinition == null) {
			DynamicStruct dynamicStruct = new DynamicStruct();
			dynamicStruct.name = name;
			return dynamicStruct;
		}
		
		try {
			Class<?> type = Class.forName(structDefinition.javaClass, true, classLoader);

			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RpcServiceException(e);
		}
	}

	private void addField(Object struct, FieldData fieldData) {
		if (struct instanceof DynamicStruct) {
			DynamicStruct dynamicStruct = (DynamicStruct) struct;
			dynamicStruct.setField(fieldData.name, fieldData.value);
			return;
		}
		
		try {
			Class<?> type = struct.getClass();

			Method method = findSetterMethod(type, fieldData);
			if (method != null) {
				Object value = convertToLocal(fieldData.value, method.getParameters()[0].getType());
				method.invoke(struct, value);
			}
			
			Field field = type.getField(fieldData.name);
			field.setAccessible(true);
			Object value = convertToLocal(fieldData.value, field.getType());
			field.set(struct, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException e) {
			throw new RpcServiceException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToRemote(Object value, Class<?> remoteType) {
		if (value == null) {
			return null;
		}

		Class<? extends Object> localType = value.getClass();
		Adapter adapter = metaDataService.findAdapterByLocalType(localType);
		if (adapter != null) {
			value = adapter.convertLocalToRemote(value);
		}

		return value;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertToLocal(Object value, Class<?> localType) {
		if (value == null) {
			return null;
		}
		
		value = autoConvert(value, localType);
		
		Adapter adapter = metaDataService.findAdapterByLocalType(localType);
		if (adapter != null) {
			value = adapter.convertRemoteToLocal(value);
		}
		
		return value;
	}

	@SuppressWarnings("rawtypes")
	private Object autoConvert(Object value, Class<?> targetType) {
		if (value instanceof List && targetType == Object[].class) {
			return ((List) value).toArray();
		}
		
		return value;
	}

	private Method findSetterMethod(Class<?> type, FieldData field) {
		String propertyName = toPropertyName(field.name);
		String methodName = "set" + propertyName;

		if (field.value != null) {
			try {
				Method method = type.getMethod(methodName, field.value.getClass());
				return method;
			} catch (NoSuchMethodException | SecurityException e) {
				// ignore
			}
		}
		
		for (Method method : type.getMethods()) {
			if (field.name.equals(method.getName()) && method.getParameterTypes().length == 1) {
				return method;
			}
		}

		return null;
	}
	
	private String toPropertyName(String name) {
		if (name.length() > 0) {
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		} else {
			return name;
		}
	}

	private FieldData readField(StructureReader reader) {
		FieldData field = new FieldData();
		
		field.name = reader.readString();
		field.value = read(reader);
		
		return field;
	}
	
	private static class FieldData {
		public String name;
		public Object value;
		
		@Override
		public String toString() {
			return "FieldDefinition [name=" + name + ", value=" + value + "]";
		}
	}
}
