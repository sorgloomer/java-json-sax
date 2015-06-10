package parser;

import java.io.IOException;

public class ValueCallback {
	public void hitString(String value){};
	public void hitInt(long value){};
	public void hitFloat(double value){};
	public void hitBoolean(boolean value){};
	public void hitNull(){};
	public void hitArray(ArrayReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_ARRAY);
	};
	public void hitObject(ObjectReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_OBJECT);
	};
	
	public static int getInt(JsonSaxReader reader) throws IOException {
		return (int)getLong(reader);
	}
	public static long getLong(JsonSaxReader reader) throws IOException {
		ValueCallbacks.IntCallback cb = new ValueCallbacks.IntCallback();
		reader.readValue(cb);
		return cb.result;
	}
	public static float getFloat(JsonSaxReader reader) throws IOException {
		return (float)getDouble(reader);
	}
	public static double getDouble(JsonSaxReader reader) throws IOException {
		ValueCallbacks.FloatCallback cb = new ValueCallbacks.FloatCallback();
		reader.readValue(cb);
		return cb.result;
	}
	public static boolean getBoolean(JsonSaxReader reader) throws IOException {
		ValueCallbacks.BoolCallback cb = new ValueCallbacks.BoolCallback();
		reader.readValue(cb);
		return cb.result;
	}
	public static String getString(JsonSaxReader reader) throws IOException {
		ValueCallbacks.StringCallback cb = new ValueCallbacks.StringCallback();
		reader.readValue(cb);
		return cb.result;
	}
	
	public static <T> T getObject(JsonSaxReader reader, ObjectBuilder<T, ?> builder) throws IOException {
		ValueCallbacks.ObjectCallback<T> cb = new ValueCallbacks.ObjectCallback<>(builder);
		reader.readValue(cb);
		return cb.result;
	}
	public static <T> T getArray(JsonSaxReader reader, ArrayBuilder<T, ?> builder) throws IOException {
		ValueCallbacks.ArrayCallback<T> cb = new ValueCallbacks.ArrayCallback<>(builder);
		reader.readValue(cb);
		return cb.result;
	}
}
