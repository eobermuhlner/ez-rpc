package JavaSerializableProtocol;

import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.protocol.java.JavaSerializableProtocol;

public class JavaSerializableProtocolTest extends AbstractProtocolTest {

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Protocol<T> getProtocol() {
		return (Protocol<T>) new JavaSerializableProtocol(getClass().getClassLoader());
	}
}
