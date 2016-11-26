package ch.obermuhlner.rpc.service;

import ch.obermuhlner.rpc.meta.ServiceMetaData;
import ch.obermuhlner.rpc.protocol.JavaSerializableProtocol;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureReader;
import ch.obermuhlner.rpc.protocol.structure.BinaryStructureWriter;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;

public class ProtocolFactory {

	public static JavaSerializableProtocol javaSerializationProtocol(ClassLoader classLoader) {
		return new JavaSerializableProtocol(classLoader);
	}
	
	public static StructureProtocol<Object> binaryProtocol(ServiceMetaData serviceMetaData, ClassLoader classLoader) {
		return new StructureProtocol<Object>(
				serviceMetaData,
				(in) -> new BinaryStructureReader(in),
				(out) -> new BinaryStructureWriter(out),
				classLoader);
	}
}
