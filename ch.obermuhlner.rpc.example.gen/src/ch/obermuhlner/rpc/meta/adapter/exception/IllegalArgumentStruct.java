package ch.obermuhlner.rpc.meta.adapter.exception;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.obermuhlner.rpc.annotation.RpcField;
import ch.obermuhlner.rpc.annotation.RpcStruct;

@RpcStruct(name = "IllegalArgument")
public class IllegalArgumentStruct {

   @RpcField()
   public String message;

}
