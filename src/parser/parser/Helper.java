package parser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Helper {
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(List<T> list, Class<T> clazz) {
		return list.toArray((T[])Array.newInstance(clazz, list.size()));
	}

	public static class IntArrayTemp {
		private int length;
		private int[] array;
		public IntArrayTemp(int cap) {
			this.length = 0;
			this.array = new int[cap];
		}
		public void append(int val) {
			if (this.length >= this.array.length) {
				this.array = Arrays.copyOf(this.array, this.array.length * 4);
			}
			this.array[this.length++] = val;
		}
		public int[] end() {
			return Arrays.copyOf(this.array, this.length);
		}
	}
	
	public static final ArrayBuilder<int[], IntArrayTemp> INT_ARRAY = new ArrayBuilder<int[], IntArrayTemp>(){
		@Override
		public IntArrayTemp begin() {
			return new IntArrayTemp(128);
		}
		@Override
		public void hitInt(IntArrayTemp obj, long value) {
			obj.append((int)value);
		}
		@Override
		public int[] end(IntArrayTemp obj) {
			return obj.end();
		}
	};
	
	public static final ArrayBuilder<Void, Void> SKIP_ARRAY = new ArrayBuilder<Void, Void>(){
		@Override
		public Void begin() {
			return null;
		}
		@Override
		public Void end(Void obj) {
			return null;
		}
	};
	
}
