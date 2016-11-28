package ch.obermuhlner.rpc.meta.adapter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeAdapter implements Adapter<LocalDateTime, EpochMillisecondStruct> {

	@Override
	public Class<LocalDateTime> getLocalType() {
		return LocalDateTime.class;
	}

	@Override
	public Class<EpochMillisecondStruct> getRemoteType() {
		return EpochMillisecondStruct.class;
	}

	@Override
	public EpochMillisecondStruct convertLocalToRemote(LocalDateTime local) {
		EpochMillisecondStruct remote = new EpochMillisecondStruct();
		remote.milliseconds = local.toInstant(ZoneOffset.UTC).toEpochMilli();
		return remote;
	}

	@Override
	public LocalDateTime convertRemoteToLocal(EpochMillisecondStruct remote) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(remote.milliseconds), ZoneOffset.UTC);
	}

}
