package parser;

import java.io.IOException;


public interface ArrayReader {
	<T> T read(ArrayBuilder<T, ?> builder) throws IOException;
}
