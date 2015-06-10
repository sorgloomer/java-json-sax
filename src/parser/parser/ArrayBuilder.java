package parser;

import java.io.IOException;

public class ArrayBuilder<T, Temp> {
	public Temp begin() { return null; };
	public void hitString(Temp obj, String value){};
	public void hitInt(Temp obj, long value){};
	public void hitFloat(Temp obj, double value){};
	public void hitBoolean(Temp obj, boolean value){};
	public void hitNull(Temp obj){};
	public void hitArray(Temp obj, ArrayReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_ARRAY);
	};
	public void hitObject(Temp obj, ObjectReader reader) throws IOException {
		reader.read(CommonCommands.SKIP_OBJECT);
	};
	public T end(Temp obj) { return null; };
}
