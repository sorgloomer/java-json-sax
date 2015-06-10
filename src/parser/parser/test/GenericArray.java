package parser.test;
import java.lang.reflect.Array;


public class GenericArray {
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
