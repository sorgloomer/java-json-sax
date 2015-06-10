package parser.test;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import parser.JsonSaxReader;
import parser.ValueCallback;

import com.fasterxml.jackson.databind.ObjectMapper;


public class SaxTest {
	public static void test1() throws IOException {
		String json = "[{'b':5,'a':'abc'},6,7.5,[null]]".replace("'", "\"");
		Object o = ValueCallback.getAny(new JsonSaxReader(new StringReader(json)));
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		mapper.writeValue(sw, o);
		String json2 = sw.toString();
		System.out.println("json: " + json);
		System.out.println("json2: " + json2);
	}
}
