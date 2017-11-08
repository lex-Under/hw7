package hw7.model;

import java.io.Serializable;
import java.util.List;

public class Publisher implements Serializable {

    private String name;
    private List<Book> publishedBooks;

    public String getName() {
        return name;
    }

    public List<Book> getPublishedBooks() {
        return publishedBooks;
    }

    public Publisher(String name, List<Book> books) {
        this.name = name;
        this.publishedBooks = books;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("name: " + name
                + "\n published books: ");
        for (Book b : publishedBooks) {
            s.append(b.getName()).append("; ");
        }
        return s.toString();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res
                + ((name != null)
                ? name.hashCode()
                : 0);
        for (Book b : publishedBooks) {
            res = 31 * res
                    + ((b != null)
                    ? b.hashCode()
                    : 0);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Publisher)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            Publisher oPublisher = (Publisher) o;
            return ((name == null) ? oPublisher.getName() == null : name.equals(oPublisher.getName()))
                    && ((publishedBooks == null)
                    ? oPublisher.getPublishedBooks() == null
                    : publishedBooks.equals(oPublisher.getPublishedBooks()));
        }
    }
}
