package ch.obermuhlner.rpc.transport;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class LocalTransport extends ServerTransportImpl implements ClientTransport {

	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> receive(request));
	}
}
