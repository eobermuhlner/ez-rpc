package ch.obermuhlner.rpc.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

import ch.obermuhlner.rpc.RpcException;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;

public class SocketClientTransport implements ClientTransport {

	private Protocol<Object> protocol;
	private String host;
	private int port;

	public SocketClientTransport(Protocol<Object> protocol, String host, int port) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
	}
	
	@Override
	public CompletableFuture<Response> send(Request request) {
		return CompletableFuture.supplyAsync(() -> {
			try (Socket socket = new Socket(host, port)) {
				OutputStream out = socket.getOutputStream();
				
				ByteArrayOutputStream requestByteArrayOutputStream = new ByteArrayOutputStream();
				protocol.serialize(requestByteArrayOutputStream, request);
				byte[] requestData = requestByteArrayOutputStream.toByteArray();
				byte[] sizeData = ByteUtils.toBytes(requestData.length);
				out.write(sizeData);
				out.write(requestData);
				out.flush();
				requestData = null;
				
				InputStream in = socket.getInputStream();
				in.read(sizeData);
				int responseSize = ByteUtils.toInt(sizeData);
				byte[] responseData = new byte[responseSize];
				in.read(responseData);
				
				Response response = (Response) protocol.deserialize(new ByteArrayInputStream(responseData));
				return response;
			} catch (IOException e) {
				throw new RpcException(e);
			}
		});
	}
}
