package ch.obermuhlner.rpc.protocol;

/**
 * Protocol converts an object into a serialized form and from the serialized form back into an object.
 *
 * @param <T> the type of the object to convert
 */
public interface Protocol {

	/**
	 * Serializes an object into a byte array.
	 * 
	 * @param element the object to serializable, or <code>null</code>
	 * @return the serialized representation
	 */
	byte[] serialize(Object element);
	
	/**
	 * Deserializes a byte array into an object.
	 * 
	 * @param data the serialized representation
	 * @return the deserialized object, or <code>null</code>
	 */
	Object deserialize(byte[] data);
}
