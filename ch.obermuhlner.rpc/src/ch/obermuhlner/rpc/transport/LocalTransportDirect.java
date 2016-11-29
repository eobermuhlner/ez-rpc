package ch.obermuhlner.rpc.transport;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransportDirect extends ServerTransportImpl implements ClientTransport {

	public LocalTransportDirect(MetaDataService metaDataService) {
		super(metaDataService);
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> receive(request));
	}
}
