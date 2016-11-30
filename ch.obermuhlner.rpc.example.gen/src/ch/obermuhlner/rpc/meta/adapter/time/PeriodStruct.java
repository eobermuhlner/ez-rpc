// Generated with ch.obermuhlner.rpc.annotation.generator.java.JavaRpcGenerator 2016-11-30T07:41:25.055

package ch.obermuhlner.rpc.meta.adapter.time;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "Period")
public class PeriodStruct {

   @RpcField()
   public int years;

   @RpcField()
   public int months;

   @RpcField()
   public int days;

}
