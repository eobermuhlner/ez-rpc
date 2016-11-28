package ch.obermuhlner.rpc.meta.adapter;

import java.util.Date;

import ch.obermuhlner.rpc.annotation.RpcStruct;
import ch.obermuhlner.rpc.meta.adapter.DateAdapter.DateStruct;

public class DateAdapter implements Adapter<Date, DateStruct> {

	@RpcStruct(name = "Date")
	public static class DateStruct {
		public long milliseconds;
	}

	@Override
	public Class<Date> getLocalType() {
		return Date.class;
	}

	@Override
	public Class<DateStruct> getRemoteType() {
		return DateStruct.class;
	}

	@Override
	public DateStruct convertLocalToRemote(Date local) {
		DateStruct remote = new DateStruct();
		remote.milliseconds = local.toInstant().toEpochMilli();
		return remote;
	}

	@Override
	public Date convertRemoteToLocal(DateStruct remote) {
		Date local = new Date(remote.milliseconds);
		return local;
	}
}
