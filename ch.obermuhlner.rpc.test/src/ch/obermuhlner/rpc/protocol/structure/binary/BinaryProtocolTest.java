package ch.obermuhlner.rpc.protocol.structure.binary;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.AbstractProtocolTest;
import ch.obermuhlner.rpc.protocol.Protocol;

public class BinaryProtocolTest extends AbstractProtocolTest {

	@Override
	protected <T> Protocol<T> getProtocol() {
		MetaDataService metaDataService = new MetaDataService();
		
		metaDataService.registerEnum(TestEnum.class);
		
		return new BinaryProtocol<>(metaDataService, getClass().getClassLoader());
	}
}
