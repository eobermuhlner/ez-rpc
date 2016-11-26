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
import ch.obermuhlner.rpc.protocol.Protocol;

public class StructureProtocol<T> implements Protocol<T> {

	private final Function<InputStream, StructureReader> readerProvider;
	private final Function<OutputStream, StructureWriter> writerProvider;
	private final ClassLoader classLoader;

	public StructureProtocol(Function<InputStream, StructureReader> readerProvider, Function<OutputStream, StructureWriter> writerProvider, ClassLoader classLoader) {
		this.readerProvider = readerProvider;
		this.writerProvider = writerProvider;
		this.classLoader = classLoader;
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
				write(writer, entry.getKey());
				write(writer, entry.getValue());
			}
			writer.writeSetEnd();
		} else if (element instanceof Double) {
			writer.writeDouble((Double) element);
		} else if (element instanceof Double) {
			writer.writeInt((Integer) element);
		} else if (element instanceof Long) {
			writer.writeLong((Long) element);
		} else if (element instanceof String) {
			writer.writeString((String) element);
		} else {
			writeStruct(writer, element);
		}
	}

	private void writeStruct(StructureWriter writer, Object element) {
		Class<?> type = element.getClass();
		
		writer.writeStructBegin(getStructName(type));
		
		for (Field field : type.getFields()) {
			field.setAccessible(true);
			FieldDefinition fieldDefinition = new FieldDefinition();
			fieldDefinition.name = field.getName();
			try {
				fieldDefinition.value = field.get(element);
				write(writer, fieldDefinition);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RpcServiceException(e);
			}
		}
		
		writer.writeStructEnd();
	}

	private String getStructName(Class<?> type) {
		return type.getSimpleName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(InputStream in) {
		StructureReader reader = readerProvider.apply(in);
		
		return (T) read(reader);
	}

	private Object read(StructureReader reader) {
		Type type = reader.readType();
		
		switch (type) {
		case NULL:
			return null;
		case DOUBLE:
			return reader.readDouble();
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
			set.put(read(reader), read(reader));
		}
		
		return set;
	}
	
	private Object readStruct(StructureReader reader) {
		String name = reader.readStructBegin();

		Object struct = createStruct(name);
		Type type = reader.readType();
		while (type != Type.FIELD_STOP) {
			if (type != Type.FIELD) {
				throw new RpcServiceException("Type must be FIELD or FIELD_STOP: " + type);
			}
			
			FieldDefinition field = readField(reader);
			addField(struct, field);
			
			type = reader.readType();
		}
		
		reader.readStructEnd();
		
		return struct;
	}

	private Object createStruct(String name) {
		try {
			Object struct = Class.forName(name, false, classLoader);
			return struct;
		} catch (ClassNotFoundException e) {
			throw new RpcServiceException(e);
		}
	}

	private void addField(Object struct, FieldDefinition fieldDefinition) {
		try {
			Method method = findSetterMethod(struct.getClass(), fieldDefinition);
			if (method != null) {
					method.invoke(struct, fieldDefinition.value);
			}
			
			Field field = struct.getClass().getField(fieldDefinition.name);
			field.setAccessible(true);
			field.set(struct, fieldDefinition.value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException e) {
			throw new RpcServiceException(e);
		}
	}

	private Method findSetterMethod(Class<?> type, FieldDefinition field) {
		String propertyName = toPropertyName(field.name);
		String methodName = "set" + propertyName;

		if (field.value != null) {
			try {
				type.getMethod(methodName, field.value.getClass());
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RpcServiceException(e);
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

	private FieldDefinition readField(StructureReader reader) {
		FieldDefinition field = new FieldDefinition();
		
		field.name = reader.readString();
		field.value = read(reader);
		
		return field;
	}
	
	private static class FieldDefinition {
		public String name;
		public Object value;
	}
}
