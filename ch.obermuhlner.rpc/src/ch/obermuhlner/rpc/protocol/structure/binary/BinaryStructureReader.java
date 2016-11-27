package ch.obermuhlner.rpc.protocol.structure.binary;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.obermuhlner.rpc.RpcServiceException;
import ch.obermuhlner.rpc.protocol.structure.StructureReader;
import ch.obermuhlner.rpc.protocol.structure.StructureType;

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
	public StructureType readType() {
		try {
			int type = in.readByte();

			switch(type) {
			case BinaryStructureWriter.STRUCT:
				return StructureType.STRUCT;
			case BinaryStructureWriter.LIST:
				return StructureType.LIST;
			case BinaryStructureWriter.SET:
				return StructureType.SET;
			case BinaryStructureWriter.FIELD:
				return StructureType.FIELD;
			case BinaryStructureWriter.FIELD_STOP:
				return StructureType.FIELD_STOP;
			case BinaryStructureWriter.INT:
				return StructureType.INT;
			case BinaryStructureWriter.LONG:
				return StructureType.LONG;
			case BinaryStructureWriter.DOUBLE:
				return StructureType.DOUBLE;
			case BinaryStructureWriter.STRING:
				return StructureType.STRING;
			case BinaryStructureWriter.NULL:
				return StructureType.NULL;
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
