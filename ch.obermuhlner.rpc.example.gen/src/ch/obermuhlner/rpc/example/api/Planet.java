// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator

package ch.obermuhlner.rpc.example.api;

import ch.obermuhlner.rpc.annotation.RpcEnum;
import ch.obermuhlner.rpc.annotation.RpcEnumValue;

@RpcEnum(name = "Planet")
public enum Planet {
   @RpcEnumValue(name = "MERCURY", id=1)
   MERCURY,
   @RpcEnumValue(name = "VENUS", id=2)
   VENUS,
   @RpcEnumValue(name = "EARTH", id=3)
   EARTH,
   @RpcEnumValue(name = "MARS")
   MARS,
   @RpcEnumValue(name = "JUPITER")
   JUPITER,
   @RpcEnumValue(name = "SATURN")
   SATURN,
   @RpcEnumValue(name = "URANOS")
   URANOS,
   @RpcEnumValue(name = "NEPTUNE")
   NEPTUNE,
}
