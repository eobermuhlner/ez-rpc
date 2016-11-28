package ch.obermuhlner.rpc.meta.adapter.time;

import java.util.Date;

import ch.obermuhlner.rpc.meta.adapter.Adapter;

public class DateAdapter implements Adapter<Date, EpochMillisecondStruct> {

	@Override
	public Class<Date> getLocalType() {
		return Date.class;
	}

	@Override
	public Class<EpochMillisecondStruct> getRemoteType() {
		return EpochMillisecondStruct.class;
	}

	@Override
	public EpochMillisecondStruct convertLocalToRemote(Date local) {
		EpochMillisecondStruct remote = new EpochMillisecondStruct();
		remote.milliseconds = local.toInstant().toEpochMilli();
		return remote;
	}

	@Override
	public Date convertRemoteToLocal(EpochMillisecondStruct remote) {
		Date local = new Date(remote.milliseconds);
		return local;
	}
}
