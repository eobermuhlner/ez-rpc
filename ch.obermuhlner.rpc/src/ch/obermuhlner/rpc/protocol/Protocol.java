package ch.obermuhlner.rpc.protocol;

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
}
