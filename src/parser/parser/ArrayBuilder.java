package parser;

import java.io.IOException;

public abstract class ArrayBuilder<T, Temp> {
	public abstract Temp begin();
	public void hitString(Temp obj, String value){};
	public void hitInt(Temp obj, long value){};
	public void hitFloat(Temp obj, double value){};
	public void hitBoolean(Temp obj, boolean value){};
	public void hitNull(Temp obj){};
	public void hitArray(Temp obj, ArrayReader reader) throws IOException {};
	public void hitObject(Temp obj, ObjectReader reader) throws IOException {};
	public abstract T end(Temp obj);
}
