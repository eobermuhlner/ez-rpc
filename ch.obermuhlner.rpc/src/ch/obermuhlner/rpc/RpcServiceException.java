package ch.obermuhlner.rpc;

public class RpcServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RpcServiceException() {
		super();
	}

	public RpcServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RpcServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RpcServiceException(String message) {
		super(message);
	}

	public RpcServiceException(Throwable cause) {
		super(cause);
	}

}
