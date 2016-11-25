package ch.obermuhlner.rpc.transport;

import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransportWithProtocol extends LocalTransport {
	
	private Protocol protocol;

	public LocalTransportWithProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public Response receive(Request request) {
		byte[] requestData = protocol.serialize(request);
		
		byte[] responseData = receive(requestData);

		return (Response) protocol.deserialize(responseData);
	}

	private byte[] receive(byte[] requestData) {
		Request request = (Request) protocol.deserialize(requestData);
		Response response = super.receive(request);
		byte[] responseData = protocol.serialize(response);
		return responseData;
	}
}
