package hw7.serialization;

import hw7.model.Author;
import hw7.model.Book;
import hw7.model.Publisher;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HumanFriendlySerializer implements Serializer {

    private static final String KEY_AUTHOR = "Автор:";
    private static final String KEY_AUTHOR_NAME = "Имя:";
    private static final String KEY_AUTHOR_BIRTHDATE = "Дата рождения:";
    private static final String KEY_AUTHOR_DEATHDATE = "Дата смерти:";
    private static final String KEY_AUTHOR_GENDER = "Пол:";
    private static final String KEY_BOOK = "Книга:";
    private static final String KEY_BOOK_NAME = "Название:";
    private static final String KEY_BOOK_YEAR = "Год издания:";
    private static final String KEY_BOOK_AUTHORS = "Список авторов:";
    private static final String KEY_PUBLISHER = "Издательство:";
    private static final String KEY_PUBLISHER_NAME = "Название:";
    private static final String KEY_PUBLISHER_BOOKS = "Список книг:";
    private static final String DATE_FORMAT = "yyyy-M-d";
    private static final String MALE_SIGNATURE = "М";
    private static final String FEMALE_SIGNATURE = "Ж";

    @Override
    public void serialize(Publisher p, OutputStream out) throws IOException {
        PublisherWriter pWriter = new PublisherWriter(out);
        pWriter.writePublisher(p);
    }

    @Override
    public Publisher deserialize(InputStream in) throws IOException {
        try (Scanner s = new Scanner(in, "utf-8")) {
            PublisherParser parser = new PublisherParser(s);
            return parser.readPublisher();
        }
    }

    private class PublisherParser {

        private String currentToken;
        private final Scanner scanner;
        private final List<Author> collectedAuthors;

        public PublisherParser(Scanner scanner) {
            this.scanner = scanner;
            collectedAuthors = new ArrayList<Author>();
        }

        private Author readAuthor() {
            String name = null;
            LocalDate birthDate = null;
            LocalDate deathDate = null;
            Author.Gender gender = null;
            boolean readComplete = false;
            try {
                while (scanner.hasNext() && !readComplete) {
                    currentToken = nextLineNotEmpty();
                    if (currentToken.startsWith(KEY_AUTHOR_NAME)) {
                        name = readValue(currentToken);
                    } else if (currentToken.startsWith(KEY_AUTHOR_BIRTHDATE)) {
                        String birthDateString = readValue(currentToken);
                        birthDate = LocalDate.parse(birthDateString,
                                DateTimeFormatter.ofPattern(DATE_FORMAT));
                    } else if (currentToken.startsWith(KEY_AUTHOR_DEATHDATE)) {
                        String deathDateString = readValue(currentToken);
                        deathDate = LocalDate.parse(deathDateString,
                                DateTimeFormatter.ofPattern(DATE_FORMAT));
                    } else if (currentToken.startsWith(KEY_AUTHOR_GENDER)) {
                        String genderString = readValue(currentToken);
                        switch (genderString) {
                            case MALE_SIGNATURE:
                                gender = Author.Gender.MALE;
                                break;
                            case FEMALE_SIGNATURE:
                                gender = Author.Gender.FEMALE;
                                break;
                            default:
                                throw new SerializationException("Unexpected token when read gender: '"
                                        + currentToken + "'. Available values: '" + MALE_SIGNATURE
                                        + "', '" + FEMALE_SIGNATURE + "'");
                        }
                    } else {
                        readComplete = true;
                    }
                }
            } catch (DateTimeParseException ex) {
                throw new SerializationException(("Unexpected token when read date: '"
                        + currentToken + "'. Available format: '" + DATE_FORMAT + "'"));
            }
            if (name == null) {
                throw new SerializationException("Name was not found when read author");
            } else if (birthDate == null) {
                throw new SerializationException("Date of birth was not found when read author");
            } else if (gender == null) {
                throw new SerializationException("Gender was not found when read author");
            } else {
                Author readAuthor = new Author(name, birthDate, deathDate, gender);
                return includeAuthor(readAuthor);
            }
        }

        private Book readBook() {
            String name = null;
            Integer year = null;
            List<Author> authors = new ArrayList<>();
            boolean readComplete = false;
            while (scanner.hasNext() && !readComplete) {
                currentToken = nextLineNotEmpty();
                if (currentToken.startsWith(KEY_BOOK_NAME)) {
                    name = readValue(currentToken);
                } else if (currentToken.startsWith(KEY_BOOK_YEAR)) {
                    year = Integer.valueOf(readValue(currentToken));
                } else if (currentToken.startsWith(KEY_BOOK_AUTHORS)) {
                    currentToken = nextLineNotEmpty();
                    while (KEY_AUTHOR.equals(currentToken)) {
                        Author author = readAuthor();
                        authors.add(author);
                    }
                    readComplete = true;
                } else {
                    readComplete = true;
                }
            }
            if (name == null) {
                throw new SerializationException("Name was not found when read book");
            } else if (year == null) {
                throw new SerializationException("Published year was not found when read book");
            } else if (authors.isEmpty()) {
                throw new SerializationException("List of authors was not found or empty when read book");
            } else {
                return new Book(name, year, authors);
            }
        }

        public Publisher readPublisher() {
            String name = null;
            List<Book> books = new ArrayList<>();
            boolean readComplete = false;
            currentToken = nextLineNotEmpty();
            if (!KEY_PUBLISHER.equals(currentToken)) {
                throw new SerializationException("Publisher header: '"
                        + KEY_PUBLISHER + "' expected but was not found"
                );
            } else {
                while (scanner.hasNext() && !readComplete) {
                    currentToken = nextLineNotEmpty();
                    if (currentToken.startsWith(KEY_PUBLISHER_NAME)) {
                        name = readValue(currentToken);
                    } else if (currentToken.startsWith(KEY_PUBLISHER_BOOKS)) {
                        currentToken = nextLineNotEmpty();
                        while (KEY_BOOK.equals(currentToken)) {
                            Book book = readBook();
                            books.add(book);
                        }
                        readComplete = true;
                    } else {
                        readComplete = true;
                    }
                }
                if (name == null) {
                    throw new SerializationException("Name was not found when read publisher");
                } else if (books.isEmpty()) {
                    throw new SerializationException("List of books was not found or empty when read publisher");
                } else {
                    return new Publisher(name, books);
                }
            }
        }

        private String readValue(String keyValueString) {
            return keyValueString.substring(keyValueString.lastIndexOf(":") + 1)
                    .trim();
        }

        private String nextLineNotEmpty() {
            String token = scanner.nextLine().trim();
            while (scanner.hasNext() && "".equals(token)) {
                token = scanner.nextLine().trim();
            }
            return token;
        }

        private Author includeAuthor(Author a) {
            int indexA = collectedAuthors.indexOf(a);
            if (indexA > -1) {
                return collectedAuthors.get(indexA);
            } else {
                return a;
            }
        }

    }

    private class PublisherWriter {

        private final OutputStreamWriter writer;
        private final String nextLine = System.getProperty("line.separator");

        public PublisherWriter(OutputStream out) {
            writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        }

        public void writePublisher(Publisher p) {
            try {
                writer.write(KEY_PUBLISHER + nextLine);
                writer.write(KEY_PUBLISHER_NAME + " " + p.getName() + nextLine);
                writer.write(KEY_PUBLISHER_BOOKS + nextLine);
                p.getPublishedBooks().forEach(this::writeBook);
                writer.flush();
            } catch (IOException ex) {
                System.out.println("Exception while writing publisher: " + ex.getMessage());
            }
        }

        private void writeBook(Book b) {
            try {
                writer.write(KEY_BOOK + nextLine);
                writer.write(KEY_BOOK_NAME + " " + b.getName() + nextLine);
                writer.write(KEY_BOOK_YEAR + " " + b.getPublishedYear() + nextLine);
                writer.write(KEY_BOOK_AUTHORS + nextLine);
                b.getAuthors().forEach(this::writeAuthor);
                writer.write(nextLine);
            } catch (IOException ex) {
                System.out.println("Exception while writing book: " + ex.getMessage());
            }
        }

        private void writeAuthor(Author a) {
            try {
                writer.write(KEY_AUTHOR + nextLine);
                writer.write(KEY_AUTHOR_NAME + " " + a.getName() + nextLine);
                writer.write(KEY_AUTHOR_BIRTHDATE + " " + a.getBirthDate()
                        + nextLine);
                if (a.getDeathDate() != null) {
                    writer.write(KEY_AUTHOR_DEATHDATE + " " + a.getDeathDate()
                            + nextLine);
                }
                writer.write(KEY_AUTHOR_GENDER + " "
                        + ((a.getGender() == Author.Gender.MALE)
                                ? MALE_SIGNATURE
                                : FEMALE_SIGNATURE)
                        + nextLine + nextLine);
            } catch (IOException ex) {
                System.out.println("Exception while writing author: " + ex.getMessage());
            }
        }
    }
}
