package hw7;

import hw7.model.*;
import hw7.serialization.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Demo {

    private Publisher demoPublisher;

    public void launch() {
        Serializer serializer = new HumanFriendlySerializer();
        initPublisher();
        System.out.println("Исходный объект:");
        printPublisher(demoPublisher);
        File source = new File(Main.class.getResource("input.txt").getFile());
        try (FileOutputStream out = new FileOutputStream(source);
             FileInputStream in = new FileInputStream(source)) {
            System.out.println("Сериализация объекта...");
            serializer.serialize(demoPublisher, out);
            System.out.println("Объект сериализирован.");
            System.out.println("Десериализация объекта...");
            Publisher publisher = serializer.deserialize(in);
            System.out.println("Объект десериализирован:");
            printPublisher(publisher);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void initPublisher() {
        Author strugatskyA = new Author("Аркадий Натанович Стругацкий", LocalDate.of(1925, 8, 28), LocalDate.of(1991, 10, 12), Author.Gender.MALE);
        Author strugatskyB = new Author("Борис Натанович Стругацкий", LocalDate.of(1933, 4, 15), LocalDate.of(2012, 11, 9), Author.Gender.MALE);
        Author efremov = new Author("Иван Антонович Ефремов", LocalDate.of(1908, 4, 22), LocalDate.of(1972, 10, 5), Author.Gender.MALE);
        Author gitkovich = new Author("Вадим Константинович Гиткович", LocalDate.of(1924, 7, 6), LocalDate.of(1977, 10, 28), Author.Gender.MALE);
        Author pualo = new Author("Пуало Коэльо", LocalDate.of(1947, 8, 24), Author.Gender.MALE);
        Book nichegoNet = new Book("Ничего нет прекрасней Земли...", 1968, Arrays.asList(strugatskyA, efremov));
        Book lezvie = new Book("Лезвие бритвы", 1964, Arrays.asList(efremov));
        Book prognoz = new Book("Прогноз", 1989, Arrays.asList(strugatskyA, strugatskyB));
        Book dyavol = new Book("Дьявол среди людей", 1991, Arrays.asList(strugatskyA, strugatskyB));
        Book strana = new Book("Страна Фантазия", 1970, Arrays.asList(efremov, gitkovich));
        Book alchimik = new Book("Алхимик", 1988, Arrays.asList(pualo));
        List<Book> books = Arrays.asList(nichegoNet, lezvie, prognoz, dyavol, strana, alchimik);
        demoPublisher = new Publisher("Экспо", books);
    }

    private void printPublisher(Publisher p) {
        System.out.println(p);
        System.out.println("Книги, подробно:\n");
        p.getPublishedBooks().forEach(b -> {
            System.out.println(b);
            System.out.println("Авторы, подробно:");
            b.getAuthors().forEach(System.out::println);
            System.out.println("");
        });
    }

}
