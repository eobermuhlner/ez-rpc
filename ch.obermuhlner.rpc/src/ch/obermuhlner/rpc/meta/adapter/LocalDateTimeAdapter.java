package ch.obermuhlner.rpc.meta.adapter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeAdapter implements Adapter<LocalDateTime, DateStruct> {

	@Override
	public Class<LocalDateTime> getLocalType() {
		return LocalDateTime.class;
	}

	@Override
	public Class<DateStruct> getRemoteType() {
		return DateStruct.class;
	}

	@Override
	public DateStruct convertLocalToRemote(LocalDateTime local) {
		DateStruct remote = new DateStruct();
		remote.milliseconds = local.toInstant(ZoneOffset.UTC).toEpochMilli();
		return remote;
	}

	@Override
	public LocalDateTime convertRemoteToLocal(DateStruct remote) {
		return null;
	}

}
