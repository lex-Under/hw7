package hw7.serialization;

import hw7.model.Publisher;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class DefaultSerializer implements Serializer {

    @Override
    public void serialize(Publisher p, OutputStream out) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(p);
        }
    }

    @Override
    public Publisher deserialize(InputStream in) throws IOException {
        try (ObjectInputStream oin = new ObjectInputStream(in)) {
            return (Publisher) oin.readObject();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Exception while casting object. "
                    + ex.getMessage());
        }
    }

}
