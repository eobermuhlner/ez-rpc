package ch.obermuhlner.rpc.protocol.structure;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.obermuhlner.rpc.RpcServiceException;

public class BinaryStructureReader implements StructureReader {

	private final DataInputStream in;

	public BinaryStructureReader(InputStream in) {
		this.in = new DataInputStream(in);
	}
	@Override
	public void readMessageBegin() {
		// ignore
	}
	
	@Override
	public void readMessageEnd() {
		// ignore
	}
	
	@Override
	public Type readType() {
		try {
			int type = in.readByte();

			switch(type) {
			case BinaryStructureWriter.STRUCT:
				return Type.STRUCT;
			case BinaryStructureWriter.LIST:
				return Type.LIST;
			case BinaryStructureWriter.SET:
				return Type.SET;
			case BinaryStructureWriter.FIELD:
				return Type.FIELD;
			case BinaryStructureWriter.FIELD_STOP:
				return Type.FIELD_STOP;
			case BinaryStructureWriter.INT:
				return Type.INT;
			case BinaryStructureWriter.LONG:
				return Type.LONG;
			case BinaryStructureWriter.DOUBLE:
				return Type.DOUBLE;
			case BinaryStructureWriter.STRING:
				return Type.STRING;
			case BinaryStructureWriter.NULL:
				return Type.NULL;
			}
			
			throw new RpcServiceException("Unknown type: " + type);
		} catch (IOException e) {
			// ignore
		}
		
		throw new RpcServiceException();
	}

	@Override
	public String readStructBegin() {
		try {
			return in.readUTF();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void readStructEnd() {
	}

	@Override
	public int readListBegin() {
		try {
			return in.readInt();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void readListEnd() {
	}

	@Override
	public int readSetBegin() {
		try {
			return in.readInt();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void readSetEnd() {
	}

	@Override
	public int readMapBegin() {
		try {
			return in.readInt();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void readMapEnd() {
		// ignore
	}

	@Override
	public void readMapEntryBegin() {
		// ignore
	}

	@Override
	public void readMapEntryEnd() {
		// ignore
	}

	@Override
	public String readFieldBegin() {
		try {
			return in.readUTF();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void readFieldEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int readInt() {
		try {
			return in.readInt();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public long readLong() {
		try {
			return in.readLong();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public Double readDouble() {
		try {
			return in.readDouble();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public String readString() {
		try {
			return in.readUTF();
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

}
