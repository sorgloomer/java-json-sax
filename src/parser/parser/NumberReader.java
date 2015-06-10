package parser;

import java.io.IOException;


public interface NumberReader {
	double readFloat() throws IOException;
	long readInt() throws IOException;
	void skip() throws IOException;
}
