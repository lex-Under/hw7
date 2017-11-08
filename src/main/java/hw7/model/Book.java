package hw7.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Book implements Serializable {

    private String name;
    private int publishedYear;
    private List<Author> authors;

    public Book(String name, int year, List<Author> authors) {
        this.name = name;
        this.publishedYear = year;
        this.authors = authors;
    }

    public String getName() {
        return name;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public int getAge() {
        return LocalDate.now().getYear() - this.publishedYear;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("name: " + name
                + ",\nyear of publiсation: " + publishedYear
                + ",\n since publiсation: " + getAge() + " years,\n authors: ");
        for (Author a : getAuthors()) {
            s.append(a.getName()).append("; ");
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
        res = 31 * res + publishedYear;
        for (Author a : authors) {
            res = 31 * res
                    + ((a != null)
                    ? a.hashCode()
                    : 0);
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            Book oBook = (Book) o;
            return (publishedYear == oBook.getPublishedYear())
                    && ((name == null) ? oBook.getName() == null : name.equals(oBook.getName()))
                    && ((authors == null) ? oBook.getAuthors() == null : authors.equals(oBook.getAuthors()));
        }
    }
}
