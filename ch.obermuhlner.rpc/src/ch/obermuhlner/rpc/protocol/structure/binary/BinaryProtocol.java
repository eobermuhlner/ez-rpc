package ch.obermuhlner.rpc.protocol.structure.binary;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.structure.StructureProtocol;

public class BinaryProtocol<T> extends StructureProtocol<T> {

	public BinaryProtocol(MetaDataService metaDataService, ClassLoader classLoader) {
		super(metaDataService, (in) -> new BinaryStructureReader(in), (out) -> new BinaryStructureWriter(out), classLoader);
	}
}
