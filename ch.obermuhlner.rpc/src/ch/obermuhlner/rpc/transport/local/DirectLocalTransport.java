package ch.obermuhlner.rpc.transport.local;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;
import ch.obermuhlner.rpc.transport.ClientTransport;
import ch.obermuhlner.rpc.transport.ServerTransportImpl;

/**
 * Local transport layer (when client and server are in the same JVM) that calls the server implementation directly without going through a Protocol to serialize/deserialize.
 * 
 * This is the fastest implementation of a local transport.
 */
public class DirectLocalTransport extends ServerTransportImpl implements ClientTransport {

	public DirectLocalTransport(MetaDataService metaDataService) {
		super(metaDataService);
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> receive(request));
	}
}
