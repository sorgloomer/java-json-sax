package parser;

import java.io.IOException;
import java.io.Reader;

public class JsonSaxReader implements ArrayReader, ObjectReader {
	
	private Reader reader;
	private int _preread = Integer.MIN_VALUE;
	private StringBuilder builder = new StringBuilder();

	private int hit_type;
	private String hit_string;
	private boolean hit_boolean;
	private long hit_long;
	private double hit_double;

	private String readString() throws IOException {
		this.eatWhitespace();
		return this.readStringCont();
	}

	private String readStringCont() throws IOException {
		this.builder.setLength(0);
		for (;;) {
			int c = this.readChar();
			if (c == '"' || c < 0)
				break;
			this.builder.append((char) c);
		}
		return this.builder.toString();
	}

	private void readColon() throws IOException {
		this.eatWhitespace();
	}

	private boolean readComma() throws IOException {
		return this.eatWhitespace() == ',';
	}

	private boolean determineIfEmpty() throws IOException {
		int c = this.eatWhitespace();
		if (c == '}' || c == ']') {
			return true;
		} else {
			this._preread = c;
			return false;
		}
	}

	private void skipWord() throws IOException {
		for (boolean first = true;; first = false) {
			int c = first ? this.readChar() : this.readCharUnsafe();
			if (c < 0 || !Character.isAlphabetic((char) c)) {
				this._preread = c;
				return;
			}
			;
		}
	}

	private <Temp> void readMember(Temp obj, String key,
			ObjectBuilder<?, Temp> builder) throws IOException {
		this.readValue();
		switch (this.hit_type) {
		case 1:
			builder.hitString(obj, key, this.hit_string);
			break;
		case 2:
			builder.hitArray(obj, key, this);
			break;
		case 3:
			builder.hitObject(obj, key, this);
			break;
		case 4:
			builder.hitBoolean(obj, key, this.hit_boolean);
			break;
		case 5:
			builder.hitNull(obj, key);
			break;
		case 6:
			builder.hitInt(obj, key, this.hit_long);
			break;
		case 7:
			builder.hitFloat(obj, key, this.hit_double);
			break;
		}
	}

	private <Temp> void readItem(Temp obj, ArrayBuilder<?, Temp> builder)
			throws IOException {
		this.readValue();
		switch (this.hit_type) {
		case 1:
			builder.hitString(obj, this.hit_string);
			break;
		case 2:
			builder.hitArray(obj, this);
			break;
		case 3:
			builder.hitObject(obj, this);
			break;
		case 4:
			builder.hitBoolean(obj, this.hit_boolean);
			break;
		case 5:
			builder.hitNull(obj);
			break;
		case 6:
			builder.hitInt(obj, this.hit_long);
			break;
		case 7:
			builder.hitFloat(obj, this.hit_double);
			break;
		}
	}

	private static boolean isNumberPart(char c) {
		return c == '.' || Character.isDigit(c);
	}

	private static boolean isNumberHead(char c) {
		return c == '+' || c == '-' || isNumberPart(c);
	}

	private int readNumberSign() throws IOException {
		int c = this.readChar();
		switch (c) {
		case '+':
			return 1;
		case '-':
			return -1;
		default:
			this._preread = c;
			return 1;
		}
	}

	private void readNumber() throws IOException {
		boolean inting = true;
		long templ = 0;
		double tempd = 0;
		double prec = 1;
		int digit;
		int sign = readNumberSign();
		for (boolean first = true;; first = false) {
			int c = first ? this.readChar() : this.readCharUnsafe();

			if (c < 0 || !isNumberPart((char) c)) {
				this._preread = c;
				if (inting) {
					this.hit_type = 6;
					this.hit_long = templ * sign;
				} else {
					this.hit_type = 7;
					this.hit_double = (templ + tempd) * sign;
				}
				return;
			}

			if (Character.isDigit((char) c)) {
				digit = c - '0';
				if (inting) {
					templ = templ * 10 + digit;
				} else {
					prec *= 0.1;
					tempd += prec * digit;
				}
			} else {
				inting = false;
			}
		}
	}

	private void readValue() throws IOException {
		int c = this.eatWhitespace();
		if (c < 0)
			return;
		switch (c) {
		case '"':
			this.hit_type = 1;
			this.hit_string = this.readStringCont();
			break;
		case '[':
			this.hit_type = 2;
			break;
		case '{':
			this.hit_type = 3;
			break;
		case 't':
		case 'T':
			this.hit_type = 4;
			this.hit_boolean = true;
			this.skipWord();
			break;
		case 'f':
		case 'F':
			this.hit_type = 4;
			this.hit_boolean = false;
			this.skipWord();
			break;
		case 'n':
		case 'N':
			this.hit_type = 5;
			this.skipWord();
			break;
		default:
			if (isNumberHead((char) c)) {
				this._preread = c;
				this.readNumber();
			} else {
				this.hit_type = 0;
			}
			break;
		}
	}

	public <T, Temp> T readObjectCont(ObjectBuilder<T, Temp> builder)
			throws IOException {
		Temp temp = builder.begin();

		if (!this.determineIfEmpty()) {
			for (;;) {
				String key = this.readString();
				this.readColon();
				this.readMember(temp, key, builder);
				if (!this.readComma())
					break;
			}
		}
		return builder.end(temp);
	}

	public <T, Temp> T readArrayCont(ArrayBuilder<T, Temp> builder)
			throws IOException {
		Temp temp = builder.begin();
		if (!this.determineIfEmpty()) {
			for (;;) {
				this.readItem(temp, builder);
				if (!this.readComma())
					break;
			}
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
}