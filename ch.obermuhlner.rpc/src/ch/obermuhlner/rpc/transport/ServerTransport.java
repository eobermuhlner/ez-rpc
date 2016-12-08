package ch.obermuhlner.rpc.transport;

import java.util.function.Consumer;

import ch.obermuhlner.rpc.service.CancelRequest;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public interface ServerTransport {

	<Service, Session> void register(Class<Service> serviceType, Service service, Consumer<Session> sessionConsumer);

	Response receive(Request request);
	
	void receiveCancel(CancelRequest cancelRequest);
	
}
