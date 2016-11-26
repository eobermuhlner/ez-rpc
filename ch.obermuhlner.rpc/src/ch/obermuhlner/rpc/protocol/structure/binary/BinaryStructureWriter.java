package ch.obermuhlner.rpc.protocol.structure.binary;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ch.obermuhlner.rpc.protocol.structure.StructureWriter;

public class BinaryStructureWriter implements StructureWriter {

	public static final int NULL = 0;
	public static final int STRUCT = 1;
	public static final int LIST = 2;
	public static final int SET = 3;
	public static final int MAP = 4;
	public static final int FIELD = 5;
	public static final int FIELD_STOP = 6;
	public static final int INT = 7;
	public static final int LONG = 8;
	public static final int DOUBLE = 9;
	public static final int STRING = 10;

	private final DataOutputStream out;

	public BinaryStructureWriter(OutputStream out) {
		this.out = new DataOutputStream(out);
	}

	@Override
	public void writeMessageBegin() {
		// does nothing
	}

	@Override
	public void writeMessageEnd() {
		// does nothing
	}

	@Override
	public void writeStructBegin(String typeName) {
		try {
			out.writeByte(STRUCT);
			out.writeUTF(typeName);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeStructEnd() {
		// does nothing
	}

	@Override
	public void writeListBegin(int size) {
		try {
			out.writeByte(LIST);
			out.writeInt(size);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeListEnd() {
		// does nothing
	}

	@Override
	public void writeSetBegin(int size) {
		try {
			out.writeByte(SET);
			out.writeInt(size);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeSetEnd() {
		// does nothing
	}

	@Override
	public void writeMapBegin(int size) {
		try {
			out.writeByte(MAP);
			out.writeInt(size);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeMapEnd() {
		// does nothing
	}

	@Override
	public void writeMapEntryBegin() {
		// does nothing
	}

	@Override
	public void writeMapEntryEnd() {
		// does nothing
	}

	@Override
	public void writeFieldBegin(String name) {
		try {
			out.writeByte(FIELD);
			out.writeUTF(name);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeFieldEnd() {
		// does nothing
	}

	@Override
	public void writeFieldStop() {
		try {
			out.writeByte(FIELD_STOP);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeInt(int value) {
		try {
			out.writeByte(INT);
			out.writeInt(value);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeLong(long value) {
		try {
			out.writeByte(LONG);
			out.writeLong(value);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeDouble(double value) {
		try {
			out.writeByte(DOUBLE);
			out.writeDouble(value);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeString(String value) {
		try {
			out.writeByte(STRING);
			out.writeUTF(value);
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void writeNull() {
		try {
			out.writeByte(NULL);
		} catch (IOException e) {
			// ignore
		}
	}
}
