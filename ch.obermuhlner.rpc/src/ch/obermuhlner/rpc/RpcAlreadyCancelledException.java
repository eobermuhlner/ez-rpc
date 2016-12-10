package ch.obermuhlner.rpc;

public class RpcAlreadyCancelledException extends RpcException {

	private static final long serialVersionUID = 1L;

	public RpcAlreadyCancelledException(String message) {
		super(message);
	}
}
