package ch.obermuhlner.rpc.transport.local;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;
import ch.obermuhlner.rpc.transport.ClientTransport;
import ch.obermuhlner.rpc.transport.ServerTransportImpl;

public class LocalTransportDirect extends ServerTransportImpl implements ClientTransport {

	public LocalTransportDirect(MetaDataService metaDataService) {
		super(metaDataService);
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> receive(request));
	}
}
