package ch.obermuhlner.rpc.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransport extends ServerTransportImpl implements ClientTransport {

	private Protocol<Object> protocol;

	public LocalTransport(Protocol<Object> protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {		
		return CompletableFuture.supplyAsync(() -> {
			byte[] requestData = toBytes(request);
			byte[] responseData = send(requestData);
			return (Response) toObject(responseData);
		});
	}
	
	private byte[] send(byte[] requestData) {
		Request request = (Request) toObject(requestData);
		Response response = receive(request);
		return toBytes(response);
	}

	private byte[] toBytes(Object object) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		protocol.serialize(byteArrayOutputStream, object);
		return byteArrayOutputStream.toByteArray();
	}

	private Object toObject(byte[] data) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		return protocol.deserialize(byteArrayInputStream);
	}
}
