package ch.obermuhlner.rpc.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Uses Java {@link Serializable serialization} as {@link Protocol} to convert an object into a serialized representation and back. 
 *
 * @param <T> the type of the object to convert
 */
public class SerializableProtocol implements Protocol {

	private final ClassLoader classLoader;

	/**
	 * Constructs a {@link SerializableProtocol} that uses the specified {@link ClassLoader} to deserialize the object.
	 * 
	 * @param classLoader the {@link ClassLoader} to create deserialized object instances, or <code>null</code> to use the standard Java class loader.
	 */
	public SerializableProtocol(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	@Override
	public byte[] serialize(Object source) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(512);
		serialize(source, byteStream);
		return byteStream.toByteArray();
	}

	@Override
	public Object deserialize(byte[] source) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(source);
		return deserialize(inputStream, classLoader);
	}
	
	/**
	 * Returns the {@link ClassLoader}.
	 * 
	 * @return the {@link ClassLoader} or <code>null</code> if none
	 */
	protected ClassLoader getClassLoader() {
		return classLoader;
	}
	
	/**
	 * Deserializes an {@link InputStream} using the specified {@link ClassLoader}.
	 * 
	 * @param inputStream the {@link InputStream} to deserialize
	 * @param classLoader the {@link ClassLoader} to create deserialized object instances, or <code>null</code> to use the standard Java class loader.
	 * @return the deserialized object
	 */
	public static Object deserialize(InputStream inputStream, ClassLoader classLoader) {
		ObjectInputStream in = null;
		try {
			in = new ClassLoaderObjectInputStream(inputStream, classLoader);
			return in.readObject();
		}
		catch (ClassNotFoundException exception) {
			throw new IllegalArgumentException(exception);
		}
		catch (IOException exception) {
			throw new IllegalArgumentException(exception);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException exception) {
					// ignore
				}
			}
		}
	}

	/**
	 * Serializes an object into an {@link OutputStream}.
	 * @param source the object to serialize, or <code>null</code>
	 * @param outputStream the {@link OutputStream} to serialize into
	 */
	public static void serialize(Object source, OutputStream outputStream) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(outputStream);
			out.writeObject(source);

		} catch (IOException exception) {
			throw new IllegalArgumentException(exception);
		}
	}
}
