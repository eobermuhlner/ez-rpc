package ch.obermuhlner.rpc.protocol.structure;

public interface StructureWriter {

	void writeMessageBegin();
	void writeMessageEnd();

	void writeStructBegin(String typeName);
	void writeStructEnd();

	void writeListBegin(int size);
	void writeListEnd();

	void writeSetBegin(int size);
	void writeSetEnd();

	void writeMapBegin(int size);
	void writeMapEnd();

	void writeMapEntryBegin();
	void writeMapEntryEnd();

	void writeFieldBegin(String name);
	void writeFieldEnd();
	void writeFieldStop();
	
	void writeInt(int value);
	void writeLong(long value);
	void writeDouble(double value);
	void writeString(String value);
	void writeNull();
}
