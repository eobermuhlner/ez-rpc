package ch.obermuhlner.rpc.protocol.structure;

public interface StructureReader {

	StructureType readType();

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
	
	void readMapEntryBegin();
	void readMapEntryEnd();
	
	String readFieldBegin();
	void readFieldEnd();

	String readEnumBegin();
	String readEnumValue();
	void readEnumEnd();

	boolean readBoolean();
	int readInt();
	long readLong();
	Double readDouble();
	String readString();
	
	default void expectType(StructureType expectedType) {
		StructureType type = readType();
		if (type != expectedType) {
			throw new IllegalArgumentException();
		}
	}
}
