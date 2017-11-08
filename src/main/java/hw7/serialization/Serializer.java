package hw7.serialization;

import hw7.model.Publisher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public interface Serializer {

    public void serialize(Publisher p, OutputStream out) throws IOException;

    public Publisher deserialize(InputStream in) throws IOException;
}
