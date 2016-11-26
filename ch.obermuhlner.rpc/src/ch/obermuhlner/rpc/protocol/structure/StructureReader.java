package ch.obermuhlner.rpc.protocol.structure;

public interface StructureReader {

	Type readType();

	void readMessageBegin();
	void readMessageEnd();

	String readStructBegin();
	void readStructEnd();
	
	int readListBegin();
	void readListEnd();
	
	int readSetBegin();
	void readSetEnd();
	
	int readMapBegin();
	void readMapEnd();
	
	String readFieldBegin();
	void readFieldEnd();
	
	int readInt();
	long readLong();
	Double readDouble();
	String readString();
	
	default void expectType(Type expectedType) {
		Type type = readType();
		if (type != expectedType) {
			throw new IllegalArgumentException();
		}
	}
}
