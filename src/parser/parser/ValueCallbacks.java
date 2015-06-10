package parser;

import java.io.IOException;

public class ValueCallbacks {
	public static class IntCallback extends ValueCallback {
		public long result;
		@Override
		public void hitInt(long value) {
			this.result = value;
		}
	}
	public static class FloatCallback extends ValueCallback {
		public double result;
		@Override
		public void hitInt(long value) {
			this.result = value;
		}
		@Override
		public void hitFloat(double value) {
			this.result = value;
		}
	}
	public static class StringCallback extends ValueCallback {
		public String result;
		@Override
		public void hitString(String value) {
			this.result = value;
		}
	}
	public static class BoolCallback extends ValueCallback {
		public boolean result;
		@Override
		public void hitBoolean(boolean value) {
			this.result = value;
		}
	}
	public static class ObjectCallback<T> extends ValueCallback {
		public T result;
		public ObjectBuilder<T, ?> builder;
		public ObjectCallback(ObjectBuilder<T, ?> builder) {
			this.builder = builder;
		}
		@Override
		public void hitObject(ObjectReader reader) throws IOException {
			this.result = reader.read(this.builder);
		}
	}
	public static class ArrayCallback<T> extends ValueCallback {
		public T result;
		public ArrayBuilder<T, ?> builder;
		public ArrayCallback(ArrayBuilder<T, ?> builder) {
			this.builder = builder;
		}
		@Override
		public void hitArray(ArrayReader reader) throws IOException {
			this.result = reader.read(this.builder);
		}
	}
	
	public static class AnyCallback extends ValueCallback {
		public Object result;
		@Override
		public void hitBoolean(boolean value) {
			this.result = value;
		}
		@Override
		public void hitInt(long value) {
			this.result = value;
		}
		@Override
		public void hitFloat(double value) {
			this.result = value;
		}
		@Override
		public void hitString(String value) {
			this.result = value;
		}
		@Override
		public void hitArray(ArrayReader reader) throws IOException {
			this.result = reader.read(CommonCommands.OBJECT_LIST);
		}
		@Override
		public void hitObject(ObjectReader reader) throws IOException {
			this.result = reader.read(CommonCommands.MAP_OBJECT);
		}
	}
	
}
