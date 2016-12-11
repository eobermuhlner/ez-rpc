package ch.obermuhlner.rpc.transport;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public interface ClientTransport {

	CompletableFuture<Response> send(Request request);
	
}
