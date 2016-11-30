package ch.obermuhlner.rpc.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Protocol converts an object into a serialized form and from the serialized form back into an object.
 *
 * @param <T> the type of the object to convert
 */
public interface Protocol<T> {

	void serialize(OutputStream out, T element);
	
	T deserialize(InputStream in);
	
	default byte[] serializeToBytes(T element) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		serialize(byteArrayOutputStream, element);
		return byteArrayOutputStream.toByteArray();
	}

	default T deserializeFromBytes(byte[] data) {
		return deserializeFromBytes(data, 0, data.length);
	}
	
	default T deserializeFromBytes(byte[] data, int offset, int length) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data, offset, length);
		return deserialize(byteArrayInputStream);
	}
}
