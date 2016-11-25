package ch.obermuhlner.rpc.transport;

import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public interface Transport {

	CompletableFuture<Response> send(Request request);
	
	Response receive(Request request);
	
	<Service> void register(Class<Service> serviceType, Service service);
}
