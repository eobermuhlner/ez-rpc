package ch.obermuhlner.rpc.meta.adapter;

public interface Adapter<LocalType, RemoteType> {

	Class<LocalType> getLocalType();
	
	Class<RemoteType> getRemoteType();

	RemoteType convertLocalToRemote(LocalType localType);
	
	LocalType convertStructToType(RemoteType remoteType);
}
