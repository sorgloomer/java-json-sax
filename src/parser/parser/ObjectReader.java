package parser;

import java.io.IOException;


public interface ObjectReader {
	<T> T read(ObjectBuilder<T, ?> builder) throws IOException;
}
