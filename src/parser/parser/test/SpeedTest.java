package parser.test;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import parser.JsonSaxReader;
import parser.ValueCallback;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SpeedTest {
	public static class Clazz {
		public Object mem;

		public Clazz(Object mem) {
			this.mem = mem;
		}
	}

	public static Object createObj(int depth) {
		if (depth >= 0) {
			return new Object[] { new Clazz(createObj(depth - 1)),
					createObj(depth - 1), createObj(depth - 2),
					createObj(depth - 3), };
		} else {
			return new Object[0];
		}
	}

	public static void test() throws IOException {
		long start, end;
		BufferedReader reader;

		System.out.println("Started...");
		Object obj = createObj(12);
		System.out.println("Memory");
		ObjectMapper mapper = new ObjectMapper();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		System.out.println("Jackson writing");
		start = System.currentTimeMillis();
		mapper.writeValue(baos, obj);
		end = System.currentTimeMillis();
		System.out.println("Writed " + (end - start) + " ms");
		System.out.println("Size: " + baos.size());

		long jackson = 0, sax = 0;
		for (int i = 0; i < 40; i++) {
			reader = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(baos.toByteArray())));
			System.out.println("Jackson reading");
			start = System.currentTimeMillis();
			mapper.readTree(reader);
			end = System.currentTimeMillis();
			System.out.println("Read " + (end - start) + " ms");
			jackson += (end - start);

			reader = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(baos.toByteArray())));
			System.out.println("Sax reading");
			start = System.currentTimeMillis();
			ValueCallback.getAny(new JsonSaxReader(reader));
			end = System.currentTimeMillis();
			System.out.println("Read " + (end - start) + " ms");
			sax += (end - start);
		}
		System.out.println("Jackson total: " + jackson);
		System.out.println("Sax total: " + sax);
	}

}
