package parser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonCommands {
	public static final int INITIAL_CAPACITY = 32;
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(List<T> list, Class<T> clazz) {
		return list.toArray((T[])Array.newInstance(clazz, list.size()));
	}
	
	public static final ArrayBuilder<Void, Void> SKIP_ARRAY = new ArrayBuilder<Void, Void>();

	public static final ObjectBuilder<Void, Void> SKIP_OBJECT = new ObjectBuilder<Void, Void>();	
	
	public static final ArrayBuilder<int[], PrimitiveLists.Int> INTS = new ArrayBuilder<int[], PrimitiveLists.Int>(){
		@Override
		public PrimitiveLists.Int begin() {
			return new PrimitiveLists.Int(INITIAL_CAPACITY);
		}
		@Override
		public void hitInt(PrimitiveLists.Int obj, long value) {
			obj.append((int)value);
		}
		@Override
		public int[] end(PrimitiveLists.Int obj) {
			return obj.end();
		}
	};
	public static final ArrayBuilder<long[], PrimitiveLists.Long> LONGS = new ArrayBuilder<long[], PrimitiveLists.Long>(){
		@Override
		public PrimitiveLists.Long begin() {
			return new PrimitiveLists.Long(INITIAL_CAPACITY);
		}
		@Override
		public void hitInt(PrimitiveLists.Long obj, long value) {
			obj.append(value);
		}
		@Override
		public long[] end(PrimitiveLists.Long obj) {
			return obj.end();
		}
	};
	public static final ArrayBuilder<float[], PrimitiveLists.Float> FLOATS = new ArrayBuilder<float[], PrimitiveLists.Float>(){
		@Override
		public PrimitiveLists.Float begin() {
			return new PrimitiveLists.Float(INITIAL_CAPACITY);
		}
		@Override
		public void hitInt(PrimitiveLists.Float obj, long value) {
			obj.append(value);
		}
		@Override
		public void hitFloat(PrimitiveLists.Float obj, double value) {
			obj.append((float)value);
		}
		@Override
		public float[] end(PrimitiveLists.Float obj) {
			return obj.end();
		}
	};
	public static final ArrayBuilder<double[], PrimitiveLists.Double> DOUBLES = new ArrayBuilder<double[], PrimitiveLists.Double>(){
		@Override
		public PrimitiveLists.Double begin() {
			return new PrimitiveLists.Double(INITIAL_CAPACITY);
		}
		@Override
		public void hitInt(PrimitiveLists.Double obj, long value) {
			obj.append(value);
		}
		@Override
		public void hitFloat(PrimitiveLists.Double obj, double value) {
			obj.append(value);
		}
		@Override
		public double[] end(PrimitiveLists.Double obj) {
			return obj.end();
		}
	};
	public static final ArrayBuilder<boolean[], PrimitiveLists.Bool> BOOLEANS = new ArrayBuilder<boolean[], PrimitiveLists.Bool>(){
		@Override
		public PrimitiveLists.Bool begin() {
			return new PrimitiveLists.Bool(INITIAL_CAPACITY);
		}
		@Override
		public void hitBoolean(PrimitiveLists.Bool obj, boolean value) {
			obj.append(value);
		}
		@Override
		public boolean[] end(PrimitiveLists.Bool obj) {
			return obj.end();
		}
	};
	public static final ArrayBuilder<ArrayList<String>, ArrayList<String>> STRINGS = new ArrayBuilder<ArrayList<String>,ArrayList<String>>(){
		@Override
		public ArrayList<String> begin() {
			return new ArrayList<String>(INITIAL_CAPACITY);
		}
		@Override
		public void hitString(ArrayList<String> obj, String value) {
			obj.add(value);
		}
		@Override
		public ArrayList<String> end(ArrayList<String> obj) {
			return obj;
		}
	};
	
	public static <T> ArrayBuilder<ArrayList<T>, ArrayList<T>> arrayOfObjects(final ObjectBuilder<T, ?> builder) {
		return new ArrayBuilder<ArrayList<T>, ArrayList<T>>() {
			@Override
			public ArrayList<T> begin() {
				return new ArrayList<T>(INITIAL_CAPACITY);
			}
			@Override
			public void hitObject(ArrayList<T> obj, ObjectReader reader) throws IOException {
				obj.add(reader.read(builder));
			}
			@Override
			public ArrayList<T> end(ArrayList<T> obj) {
				return obj;
			}
		};
	}
	public static <T> ArrayBuilder<ArrayList<T>, ArrayList<T>> arrayOfArrays(final ArrayBuilder<T, ?> builder) {
		return new ArrayBuilder<ArrayList<T>, ArrayList<T>>() {
			@Override
			public ArrayList<T> begin() {
				return new ArrayList<T>(INITIAL_CAPACITY);
			}
			@Override
			public void hitArray(ArrayList<T> obj, ArrayReader reader) throws IOException {
				obj.add(reader.read(builder));
			}
			@Override
			public ArrayList<T> end(ArrayList<T> obj) {
				return obj;
			}
		};
	}
	
	public static final ArrayBuilder<ArrayList<Object>, ArrayList<Object>> OBJECT_LIST = new ArrayBuilder<ArrayList<Object>, ArrayList<Object>>(){
		@Override
		public ArrayList<Object> begin() {
			return new ArrayList<Object>(32);
		}
		@Override
		public ArrayList<Object> end(ArrayList<Object> obj) {
			return obj;
		}
		@Override
		public void hitString(ArrayList<Object> obj, String value) {
			obj.add(value);
		}
		@Override
		public void hitInt(ArrayList<Object> obj, long value) {
			obj.add(value);
		}
		@Override
		public void hitFloat(ArrayList<Object> obj, double value) {
			obj.add(value);
		}
		@Override
		public void hitBoolean(ArrayList<Object> obj, boolean value) {
			obj.add(value);
		}
		@Override
		public void hitNull(ArrayList<Object> obj) {
			obj.add(null);
		}
		@Override
		public void hitArray(ArrayList<Object> obj, ArrayReader reader) throws IOException {
			obj.add(reader.read(OBJECT_LIST));
		}
		@Override
		public void hitObject(ArrayList<Object> obj, ObjectReader reader) throws IOException {
			obj.add(reader.read(MAP_OBJECT));
		}
	};
	
	public static final ObjectBuilder<HashMap<String, Object>, HashMap<String, Object>> MAP_OBJECT = new ObjectBuilder<HashMap<String, Object>, HashMap<String, Object>>(){
		@Override
		public HashMap<String, Object> begin() {
			return new HashMap<String, Object>();
		}
		@Override
		public HashMap<String, Object> end(HashMap<String, Object> obj) {
			return obj;
		}
		@Override
		public void hitString(HashMap<String, Object> obj, String key, String value) {
			obj.put(key, value);
		}
		@Override
		public void hitInt(HashMap<String, Object> obj, String key,  long value) {
			obj.put(key, value);
		}
		@Override
		public void hitFloat(HashMap<String, Object> obj, String key,  double value) {
			obj.put(key, value);
		}
		@Override
		public void hitBoolean(HashMap<String, Object> obj, String key,  boolean value) {
			obj.put(key, value);
		}
		@Override
		public void hitNull(HashMap<String, Object> obj, String key) {
			obj.put(key, null);
		}
		@Override
		public void hitArray(HashMap<String, Object> obj, String key,  ArrayReader reader) throws IOException {
			obj.put(key, reader.read(OBJECT_LIST));
		}
		@Override
		public void hitObject(HashMap<String, Object> obj, String key,  ObjectReader reader) throws IOException {
			obj.put(key, reader.read(MAP_OBJECT));
		}
	};
	
}
