package parser;

import java.io.IOException;
import java.io.Reader;

public class JsonSaxReader implements ArrayReader, ObjectReader {
	private static final long LONG_MAX_PER_TEN = Long.MAX_VALUE / 10;
	
	private Reader reader;
	private int _preread = Integer.MIN_VALUE;
	private StringBuilder builder = new StringBuilder();
	
	
	private String readString() throws IOException {
		this.eatWhitespace();
		return this.readStringCont();
	}
	
	private String readStringCont() throws IOException {
		this.builder.setLength(0);
		for (;;) {
			int c = this.readChar();
			if (c == '"' || c < 0) break;
			this.builder.append((char)c);
		}
		return this.builder.toString();
	}
	
	private void readColon() throws IOException {
		this.eatWhitespace();
	}
	private boolean readComma() throws IOException {
		return this.eatWhitespace() == ',';
	}
	
	private void skipWord() throws IOException {
		for (boolean first = true;;first = false) {
			int c = first ? this.readChar() : this.readCharUnsafe();
			if (c < 0 || !Character.isAlphabetic((char)c)) {
				this._preread = c;
				return;
			};
		}
	}
	
	private <T, Temp> void readMember(Temp obj, ValueStrategy<Temp, ObjectBuilder<T, Temp>> strategy, ObjectBuilder<T, Temp>  builder) throws IOException {
		String key = this.readString();
		this.readColon();
		this.readValue(obj, key, strategy, builder);
	}
	
	private <T,B> void readNumber(T obj, String key, ValueStrategy<T, B> strat, B builder) throws IOException {
		boolean inting = true;
		long templ = 0;
		double tempd = 0;
		int digit;
		
		for (boolean first = true;;first = false) {
			int c = first ? this.readChar() : this.readCharUnsafe();
			if (c < 0) break;
			
			if (Character.isDigit((char)c)) {
				digit = c - '0';
				// TODO
			}
			
			if (c < 0 || !Character.isAlphabetic((char)c)) {
				this._preread = c;
				return;
			};
		}
		
	}
	
	private <T,B> void readValue(T obj, String key, ValueStrategy<T, B> strat, B builder) throws IOException {
		int c = this.eatWhitespace();
		if (c < 0) return;
		switch (c) {
		case '"':
			String str = this.readStringCont();
			strat.hitString(obj, key, builder, str);
			break;
		case '[':
			strat.hitArray(obj, key, builder, this);
			break;
		case '{':
			strat.hitObject(obj, key, builder, this);
			break;
		case 't':
		case 'T':
			strat.hitBoolean(obj, key, builder, true);
			this.skipWord();
			break;
		case 'f':
		case 'F':
			strat.hitBoolean(obj, key, builder, false);
			this.skipWord();
			break;
		case 'n':
		case 'N':
			strat.hitNull(obj, key, builder);
			this.skipWord();
			break;
		case '-':
		case '+':
		case '.':
			this._preread = c;
			this.readNumber(obj, key, strat, builder);
			break;
		default:
			if (Character.isDigit((char)c)) {
				this._preread = c;
				this.readNumber(obj, key, strat, builder);
			}
			break;
		}
	}
	
	public <T, Temp> T readObjectCont(ObjectBuilder<T, Temp> builder) throws IOException {
		Temp temp = builder.begin();
		ValueStrategy<Temp, ObjectBuilder<T, Temp>> strategy = JsonSaxReader.strategyObject();
		for (;;) {
			this.readMember(temp, strategy, builder);
			if (!this.readComma()) break;
		}
		return builder.end(temp);
	}
	
	public <T, Temp> T readArrayCont(ArrayBuilder<T, Temp> builder) throws IOException {
		Temp temp = builder.begin();
		ValueStrategy<Temp, ArrayBuilder<T, Temp>> strategy = JsonSaxReader.strategyArray();
		for (;;) {
			this.readValue(temp, null, strategy, builder);
			if (!this.readComma()) break;
		}
		return builder.end(temp);
	}
	
	private int readChar() throws IOException {
		if (this._preread != Integer.MIN_VALUE) {
			int c = this._preread;
			this._preread = Integer.MIN_VALUE;
			return c;
		} else {
			return this.readCharUnsafe();
		}
	}
	private int readCharUnsafe() throws IOException {
		return this.reader.read();
	}
	
	private int eatWhitespace() throws IOException {
		for (;;) {
			int c = this.readChar();
			if (c < 0 || !Character.isWhitespace(c)) {
				return c;
			}
		}
	}
	
	public JsonSaxReader(Reader reader) {
		this.reader = reader;
	}
	
	
	@Override
	public <T> T read(ArrayBuilder<T, ?> builder) throws IOException {
		return this.readArrayCont(builder);
	}
	
	@Override
	public <T> T read(ObjectBuilder<T, ?> builder) throws IOException {
		return this.readObjectCont(builder);
	}
	
	private static abstract class ValueStrategy<T, B> {
		public abstract void hitString(T obj, String key, B builder, String value);
		public abstract void hitInt(T obj, String key, B builder, long value);
		public abstract void hitFloat(T obj, String key, B builder, double value);
		public abstract void hitBoolean(T obj, String key, B builder, boolean value);
		public abstract void hitNull(T obj, String key, B builder);
		public abstract void hitObject(T obj, String key, B builder, ObjectReader reader) throws IOException;
		public abstract void hitArray(T obj, String key, B builder, ArrayReader reader) throws IOException;
	}
	
	@SuppressWarnings("unchecked")
	private static <T ,Temp> ValueStrategy<Temp, ObjectBuilder<T, Temp>> strategyObject() {
		return (ValueStrategy<Temp, ObjectBuilder<T, Temp>>)OBJECT_MEMBER;
	}
	@SuppressWarnings("unchecked")
	private static <T ,Temp> ValueStrategy<Temp, ArrayBuilder<T, Temp>> strategyArray() {
		return (ValueStrategy<Temp, ArrayBuilder<T, Temp>>)ARRAY_MEMBER;
	}
	
	private static final ValueStrategy<?, ?> OBJECT_MEMBER = new ValueStrategy<Object, ObjectBuilder<Object,Object>>() {
		@Override
		public void hitInt(Object obj, String key, ObjectBuilder<Object,Object> builder, long value) {
			builder.hitInt(obj, key, value);
		};
		@Override
		public void hitString(Object obj, String key, ObjectBuilder<Object, Object> builder, String value) {
			builder.hitString(obj, key, value);
		}
		@Override
		public void hitFloat(Object obj, String key, ObjectBuilder<Object, Object> builder, double value) {
			builder.hitFloat(obj, key, value);
		}
		@Override
		public void hitNull(Object obj, String key, ObjectBuilder<Object, Object> builder) {
			builder.hitNull(obj, key);
		}		
		@Override
		public void hitBoolean(Object obj, String key,	ObjectBuilder<Object, Object> builder, boolean value) {
			builder.hitBoolean(obj, key, value);
		}
		@Override
		public void hitArray(Object obj, String key, ObjectBuilder<Object, Object> builder, ArrayReader reader) throws IOException  {
			builder.hitArray(obj, key, reader);
		}
		@Override
		public void hitObject(Object obj, String key, ObjectBuilder<Object, Object> builder, ObjectReader reader) throws IOException  {
			builder.hitObject(obj, key, reader);
		}
	};
	
	private static final ValueStrategy<?, ?> ARRAY_MEMBER = new ValueStrategy<Object, ArrayBuilder<Object,Object>>() {
		@Override
		public void hitInt(Object obj, String key, ArrayBuilder<Object,Object> builder, long value) {
			builder.hitInt(obj, value);
		};
		@Override
		public void hitString(Object obj, String key, ArrayBuilder<Object, Object> builder, String value) {
			builder.hitString(obj, value);
		}
		@Override
		public void hitFloat(Object obj, String key, ArrayBuilder<Object, Object> builder, double value) {
			builder.hitFloat(obj, value);
		}
		@Override
		public void hitNull(Object obj, String key, ArrayBuilder<Object, Object> builder) {
			builder.hitNull(obj);
		}
		
		@Override
		public void hitBoolean(Object obj, String key,ArrayBuilder<Object, Object> builder, boolean value) {
			builder.hitBoolean(obj, value);
		}
		@Override
		public void hitArray(Object obj, String key, ArrayBuilder<Object, Object> builder, ArrayReader reader) throws IOException  {
			builder.hitArray(obj, reader);
		}
		@Override
		public void hitObject(Object obj, String key, ArrayBuilder<Object, Object> builder, ObjectReader reader) throws IOException  {
			builder.hitObject(obj, reader);
		}
	};
	
}