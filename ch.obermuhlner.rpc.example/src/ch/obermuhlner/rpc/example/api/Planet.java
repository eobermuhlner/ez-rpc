package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcEnum;
import ch.obermuhlner.rpc.annotation.RpcEnumValue;

@RpcEnum
public enum Planet {
	@RpcEnumValue(id = 1)
	MERCURY,
	@RpcEnumValue(id = 2)
	VENUS,
	@RpcEnumValue(id = 3)
	EARTH,
	MARS,
	JUPITER,
	SATURN,
	URANOS,
	NEPTUNE
}
