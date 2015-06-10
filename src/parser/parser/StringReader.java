package parser;

import java.io.IOException;


public interface StringReader {
	String read() throws IOException;
	void skip() throws IOException;
}
