package ch.obermuhlner.rpc.meta.adapter.exception;

import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class IllegalArgumentExceptionAdapter implements Adapter<IllegalArgumentException, IllegalArgumentStruct> {

	@Override
	public Class<IllegalArgumentException> getLocalType() {
		return IllegalArgumentException.class;
	}

	@Override
	public Class<IllegalArgumentStruct> getRemoteType() {
		return IllegalArgumentStruct.class;
	}

	@Override
	public IllegalArgumentStruct convertLocalToRemote(IllegalArgumentException local) {
		IllegalArgumentStruct remote = new IllegalArgumentStruct();
		remote.message = local.getMessage();
		return remote;
	}

	@Override
	public IllegalArgumentException convertRemoteToLocal(IllegalArgumentStruct remote) {
		IllegalArgumentException local = new IllegalArgumentException(remote.message);
		return local;
	}

}
