package ch.obermuhlner.rpc.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ch.obermuhlner.rpc.exception.RpcException;

public class ByteUtils {

	public static byte[] toBytes(int value) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4);
		DataOutputStream out = new DataOutputStream(byteArrayOutputStream);
		
		try {
			out.writeInt(value);
		} catch (IOException e) {
			// ignore
		}
		
		return byteArrayOutputStream.toByteArray();
	}
	
	public static int toInt(byte[] data) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		DataInputStream in = new DataInputStream(byteArrayInputStream);
		
		try {
			return in.readInt();
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}

}
