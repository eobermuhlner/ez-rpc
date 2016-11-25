package ch.obermuhlner.rpc.transport;

import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public interface ServerTransport {

	<Service> void register(Class<Service> serviceType, Service service);

	Response receive(Request request);
	
}
