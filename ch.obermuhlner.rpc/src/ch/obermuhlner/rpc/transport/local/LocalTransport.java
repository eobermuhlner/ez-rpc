package ch.obermuhlner.rpc.transport.local;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.CancelRequest;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;
import ch.obermuhlner.rpc.transport.ClientTransport;
import ch.obermuhlner.rpc.transport.ServerTransportImpl;

/**
 * Local transport layer (when client and server are in the same JVM) that uses a Protocol to serialize/deserialize before calling the server implementation.
 * 
 * This is useful to test the protocol.
 * 
 * Use {@link LocalTransportDirect} if you want the most efficient transport implementation on the same JVM.
 */
public class LocalTransport extends ServerTransportImpl implements ClientTransport {

	private final Protocol<Object> protocol;

	public LocalTransport(MetaDataService metaDataService, Protocol<Object> protocol) {
		super(metaDataService);
		
		this.protocol = protocol;
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {		
		return CompletableFuture.supplyAsync(() -> {
			byte[] requestData = protocol.serializeToBytes(request);
			byte[] responseData = sendRequest(requestData);
			return (Response) protocol.deserializeFromBytes(responseData);
		});
	}
	
	@Override
	public void sendCancel(CancelRequest cancelRequest) {
		ForkJoinPool.commonPool().submit(() -> {
			byte[] cancelRequestData = protocol.serializeToBytes(cancelRequest);
			sendCancelRequest(cancelRequestData);
		});
	}
	
	private byte[] sendRequest(byte[] requestData) {
		Request request = (Request) protocol.deserializeFromBytes(requestData);
		Response response = receive(request);
		return protocol.serializeToBytes(response);
	}
	
	private void sendCancelRequest(byte[] cancelRequestData) {
		CancelRequest cancelRequest = (CancelRequest) protocol.deserializeFromBytes(cancelRequestData);
		receiveCancel(cancelRequest);
	}
}
