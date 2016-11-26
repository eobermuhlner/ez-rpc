package ch.obermuhlner.rpc.protocol.structure.xml;

import java.io.OutputStream;
import java.io.PrintStream;

import ch.obermuhlner.rpc.protocol.structure.StructureWriter;

public class XmlStructureWriter implements StructureWriter {

	private PrintStream out;

	public XmlStructureWriter(OutputStream out) {
		this.out = new PrintStream(out);
	}
	
	@Override
	public void writeMessageBegin() {
		out.print("<message>");
	}

	@Override
	public void writeMessageEnd() {
		out.print("</message>");
	}

	@Override
	public void writeStructBegin(String typeName) {
		out.print("<struct name=\"");
		out.print(sanitize(typeName));
		out.print("\">");
	}

	@Override
	public void writeStructEnd() {
		out.print("</struct>");
	}

	@Override
	public void writeListBegin(int size) {
		out.print("<list>");
	}

	@Override
	public void writeListEnd() {
		out.print("</list>");
	}

	@Override
	public void writeSetBegin(int size) {
		out.print("<set>");
	}

	@Override
	public void writeSetEnd() {
		out.print("</set>");
	}

	@Override
	public void writeMapBegin(int size) {
		out.print("<map>");
	}

	@Override
	public void writeMapEnd() {
		out.print("</map>");
	}

	@Override
	public void writeMapEntryBegin() {
		out.print("<mapentry>");
	}

	@Override
	public void writeMapEntryEnd() {
		out.print("</mapentry>");
	}

	@Override
	public void writeFieldBegin(String name) {
		out.print("<field name=\"");
		out.print(sanitize(name));
		out.print("\">");
	}

	@Override
	public void writeFieldEnd() {
		out.print("</field>");
	}

	@Override
	public void writeFieldStop() {
		// do nothing
	}

	@Override
	public void writeInt(int value) {
		out.print("<int>");
		out.print(value);
		out.print("</int>");
	}

	@Override
	public void writeLong(long value) {
		out.print("<long>");
		out.print(value);
		out.print("</long>");
	}

	@Override
	public void writeDouble(double value) {
		out.print("<double>");
		out.print(value);
		out.print("</double>");
	}

	@Override
	public void writeString(String value) {
		out.print("<string>");
		out.print(sanitize(value));
		out.print("</string>");
	}

	@Override
	public void writeNull() {
		out.print("<null/>");
	}

	private String sanitize(String typeName) {
		return typeName.replaceAll("[<>\"]", "");
	}
}
