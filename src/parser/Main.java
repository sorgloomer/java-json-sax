import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {
	
	public static class Clazz {
		public Object mem;
		public Clazz(Object mem) {
			this.mem = mem;
		}
	}
	
	public static Object createObj(int depth) {
		if (depth >= 0) {
			return new Object[] {
				new Clazz(createObj(depth - 1)),	
				createObj(depth - 1),	
				createObj(depth - 2),	
				createObj(depth - 3),	
			};
		} else {
			return new Object[0];
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("Started...");
		Object obj = createObj(10);
		System.out.println("Memory");
		ObjectMapper mapper = new ObjectMapper();
		
		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		
		System.out.println("Writing");
		long start = System.currentTimeMillis();
		mapper.writeValue(bais, obj);
		long end = System.currentTimeMillis();
		System.out.println("Writed " + (end - start) + " ms");
		System.out.println("Size: " + bais.size());
		
		// System.out.println(bais.toString("UTF-8"));
		
	}
	
	public static void testGenericArray() {
		System.out.println("int.class==int.class: " + (int.class == int.class));
		System.out.println("int.class==Integer.class: " + (int.class == Integer.class));
		String[] ss = createArray(String.class);
		Integer[] is = createArray(int.class);
	}

	public static <T> T[] createArray(Class<T> clazz) {
		return (T[])Array.newInstance(clazz, 10);
	}
}
