package ch.obermuhlner.rpc.transport.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.obermuhlner.rpc.RpcException;
import ch.obermuhlner.rpc.meta.MetaDataService;
import ch.obermuhlner.rpc.protocol.Protocol;
import ch.obermuhlner.rpc.service.CancelRequest;
import ch.obermuhlner.rpc.service.Request;
import ch.obermuhlner.rpc.service.Response;
import ch.obermuhlner.rpc.transport.ByteUtils;
import ch.obermuhlner.rpc.transport.ServerTransportImpl;

public class SocketServerTransport extends ServerTransportImpl {

	private Protocol<Object> protocol;
	
	private int port;

	private ServerSocket serverSocket;

	private ExecutorService executorService;

	public SocketServerTransport(MetaDataService metaDataService, Protocol<Object> protocol, int port) {
		this(metaDataService, protocol, port, Executors.newCachedThreadPool());
	}
	
	public SocketServerTransport(MetaDataService metaDataService, Protocol<Object> protocol, int port, ExecutorService executorService) {
		super(metaDataService);
		
		this.protocol = protocol;
		this.port = port;
		this.executorService = executorService;
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			for (;;) {
				executorService.execute(new SocketHandler(serverSocket.accept()));
			}
		} catch (IOException e) {
			executorService.shutdown();
		}
	}
	
	private class SocketHandler implements Runnable {
		private Socket socket;

		public SocketHandler(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				InputStream in = socket.getInputStream();

				byte[] sizeData = new byte[4];
				in.read(sizeData);
				int requestSize = ByteUtils.toInt(sizeData);
				
				byte[] requestData = new byte[requestSize];
				in.read(requestData);
				
				Object requestObject = protocol.deserialize(new ByteArrayInputStream(requestData));
				if (requestObject instanceof Request) {
					Request request = (Request) requestObject;
					Response response = receive(request);
					
					ByteArrayOutputStream responseByteArrayOutputStream = new ByteArrayOutputStream();
					protocol.serialize(responseByteArrayOutputStream, response);
					byte[] responseData = responseByteArrayOutputStream.toByteArray();
					
					OutputStream out = socket.getOutputStream();
					out.write(ByteUtils.toBytes(responseData.length));
					out.write(responseData);
					out.flush();
				} else if (requestObject instanceof CancelRequest) {
					CancelRequest cancelRequest = (CancelRequest) requestObject;
					receiveCancel(cancelRequest);
				}
			} catch (IOException e) {
				throw new RpcException(e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
