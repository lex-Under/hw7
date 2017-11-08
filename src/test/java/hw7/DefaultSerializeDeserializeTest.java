package hw7;

import hw7.model.*;
import hw7.serialization.DefaultSerializer;
import hw7.serialization.Serializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultSerializeDeserializeTest {

    private static Publisher publisher;
    private static File sourceTestFile;
    private static Serializer serializer;

    @BeforeClass
    public static void setup() {
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
        publisher = new Publisher("Экспо", books);
        sourceTestFile = new File(Main.class.getResource("inputTest.txt").getFile());
        serializer = new DefaultSerializer();
    }

    @Test
    public void serializeDeserializeTest() throws IOException{
        try(FileOutputStream out = new FileOutputStream(sourceTestFile);
                FileInputStream in = new FileInputStream(sourceTestFile)){
            serializer.serialize(publisher, out);
            Publisher deserialized = serializer.deserialize(in);
            Assert.assertEquals(publisher, deserialized);   
        }
    }
    
}
