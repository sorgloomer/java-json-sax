package parser;

import java.util.Arrays;

public class PrimitiveLists {
	public static class Int {
		private int length;
		private int[] array;
		public Int(int cap) {
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
	public static class Long {
		private int length;
		private long[] array;
		public Long(int cap) {
			this.length = 0;
			this.array = new long[cap];
		}
		public void append(long val) {
			if (this.length >= this.array.length) {
				this.array = Arrays.copyOf(this.array, this.array.length * 4);
			}
			this.array[this.length++] = val;
		}
		public long[] end() {
			return Arrays.copyOf(this.array, this.length);
		}
	}
	public static class Float {
		private int length;
		private float[] array;
		public Float(int cap) {
			this.length = 0;
			this.array = new float[cap];
		}
		public void append(float val) {
			if (this.length >= this.array.length) {
				this.array = Arrays.copyOf(this.array, this.array.length * 4);
			}
			this.array[this.length++] = val;
		}
		public float[] end() {
			return Arrays.copyOf(this.array, this.length);
		}
	}
	public static class Double {
		private int length;
		private double[] array;
		public Double(int cap) {
			this.length = 0;
			this.array = new double[cap];
		}
		public void append(double val) {
			if (this.length >= this.array.length) {
				this.array = Arrays.copyOf(this.array, this.array.length * 4);
			}
			this.array[this.length++] = val;
		}
		public double[] end() {
			return Arrays.copyOf(this.array, this.length);
		}
	}
	public static class Bool {
		private int length;
		private boolean[] array;
		public Bool(int cap) {
			this.length = 0;
			this.array = new boolean[cap];
		}
		public void append(boolean val) {
			if (this.length >= this.array.length) {
				this.array = Arrays.copyOf(this.array, this.array.length * 4);
			}
			this.array[this.length++] = val;
		}
		public boolean[] end() {
			return Arrays.copyOf(this.array, this.length);
		}
	}
}
