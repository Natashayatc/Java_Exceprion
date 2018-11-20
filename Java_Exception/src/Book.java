import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Book  implements Iterable<String> {

    public static class ClassCastException extends Exception{
        private String name;
        private String phone;
        public ClassCastException (String name, String phone){
            this.name = name;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return getMessage();
        }

        @Override
        public String getMessage() {
            return "This contact is already there name" + name + " phone: " + phone;
        }
    }

    private Map<String, String> book = new HashMap<>();

    @Override
    public Iterator<String> iterator() {
        return book.values().iterator();
    }

    public void addContact(String name, String phone) throws ClassCastException {
        if(book.containsKey(name)){
            throw new ClassCastException(name, phone);
        }
        book.put(name, phone);
    }

    public void remove(String name){
        book.remove(name);
    }



    public void saveTo(String path) throws IOException {
        var map = new ObjectMapper();
        var jsonResult = map.writerWithDefaultPrettyPrinter()
                .writeValueAsString(book);
        var p = Paths.get(path);
        var strToBytes = jsonResult.getBytes();
        Files.write(p, strToBytes);
    }

    public static Book open(String path) throws IOException {
        var book = new Book();
        var map = new ObjectMapper();
        var Ref = new TypeReference<HashMap<String, String>>() {};
        book.book = map.readValue(new File(path), Ref);
        return book;
    }
    public static void main(String args[]) throws IOException {
        Book book = new Book();
        try {
            book.addContact("Katya", "89754");
            book.addContact("Olesya", "77532");
            book.addContact("Dima", "77543");
            book.addContact("Oleg", "54312");

            book.saveTo("d:\\Users\\Alexander\\Desktop\\Тинькофф\\btl.txt");
            Book newBook = Book.open("d:\\Users\\Alexander\\Desktop\\Тинькофф\\btl.txt");
            for (String n : newBook) {
                System.out.println(n);
            }
        } catch (Book.ClassCastException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}