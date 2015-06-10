package parser;

import java.io.IOException;

public class ObjectBuilder<T, Temp> {
	public Temp begin() { return null; };
	public void hitString(Temp obj, String key, String value){};
	public void hitInt(Temp obj, String key, long value){};
	public void hitFloat(Temp obj, String key, double value){};
	public void hitBoolean(Temp obj, String key, boolean value){};
	public void hitNull(Temp obj, String key){};
	public void hitArray(Temp obj, String key, ArrayReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_ARRAY);
	};
	public void hitObject(Temp obj, String key, ObjectReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_OBJECT);
	};
	public T end(Temp obj) { return null; };
}
