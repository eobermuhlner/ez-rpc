package ch.obermuhlner.rpc.service;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.JavaSerializableProtocol;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryStructureReader;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryStructureWriter;

public class ProtocolFactory {

	public static JavaSerializableProtocol javaSerializationProtocol(ClassLoader classLoader) {
		return new JavaSerializableProtocol(classLoader);
	}
	
	public static StructureProtocol<Object> binaryProtocol(MetaDataService serviceMetaData, ClassLoader classLoader) {
		return new StructureProtocol<Object>(
				serviceMetaData,
				(in) -> new BinaryStructureReader(in),
				(out) -> new BinaryStructureWriter(out),
				classLoader);
	}
}
