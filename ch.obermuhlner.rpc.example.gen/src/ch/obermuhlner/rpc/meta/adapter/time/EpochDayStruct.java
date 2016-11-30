package ch.obermuhlner.rpc.meta.adapter.time;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "EpochDay")
public class EpochDayStruct {

   @RpcField()
   public long days;

}
