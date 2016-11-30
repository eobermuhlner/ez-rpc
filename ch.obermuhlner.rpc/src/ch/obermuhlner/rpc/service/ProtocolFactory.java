package ch.obermuhlner.rpc.service;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryStructureReader;
import ch.obermuhlner.rpc.protocol.structure.binary.BinaryStructureWriter;

public class ProtocolFactory {

	public static JavaSerializableProtocol javaSerializationProtocol(ClassLoader classLoader) {
		return new JavaSerializableProtocol(classLoader);
	}
	
	public static StructureProtocol<Object> binaryProtocol(MetaDataService metaDataService, ClassLoader classLoader) {
		return new StructureProtocol<Object>(
				metaDataService,
				(in) -> new BinaryStructureReader(in),
				(out) -> new BinaryStructureWriter(out),
				classLoader);
	}
}
