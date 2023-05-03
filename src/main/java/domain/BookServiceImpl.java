package domain;

import lombok.NoArgsConstructor;
import model.Author;
import model.Book;
import model.Location;

import java.util.Arrays;
import java.util.List;


public class BookServiceImpl implements BookService {

    @Override
    public List<Book> findAll() {
        Author author1 = new Author("George Orwell", null);
        Author author2 = new Author("Harper Lee", null);
        Author author3 = new Author("J.D. Salinger", null);
        Author author4 = new Author("F. Scott Fitzgerald", null);
        Author author5 = new Author("J.K. Rowling", null);
        Author author6 = new Author("J.R.R. Tolkien", null);
        Author author7 = new Author("Jane Austen", null);

        Location location1 = new Location(12345, 67890, "New York", null);
        Location location2 = new Location(23456, 78901, "Los Angeles", null);
        Location location3 = new Location(34567, 89012, "Chicago", null);
        Location location4 = new Location(45678, 90123, "San Francisco", null);

        Book book1 = new Book("1984", List.of(author1, author2), "978-0-452-28423-4", 19.99, 4, List.of(location1, location2));
        Book book2 = new Book("To Kill a Mockingbird", List.of(author2), "978-0-06-093546-7", 24.99, 5, List.of(location2));
        Book book3 = new Book("The Catcher in the Rye", List.of(author3), "978-0-316-76948-1", 29.99, 3, List.of(location3));
        Book book4 = new Book("The Great Gatsby", List.of(author4), "978-0-7432-7356-5", 34.99, 4, List.of(location4));
        Book book5 = new Book("Harry Potter and the Philosopher's Stone", List.of(author5), "978-0-590-35340-3", 18.99, 5, List.of(location1));
        Book book6 = new Book("The Hobbit", List.of(author6), "978-0-618-96864-0", 22.99, 3, List.of(location2));
        Book book7 = new Book("Pride and Prejudice", List.of(author7), "978-0-14-143951-8", 25.99, 4, List.of(location3));
        Book book8 = new Book("Animal Farm", List.of(author1), "978-0-451-52493-5", 19.99, 5, List.of(location4));
        Book book9 = new Book("The Catcher in the Rye", List.of(author3), "978-0-316-76948-1", 28.99, 3, List.of(location1));
        Book book10 = new Book("The Lord of the Rings: The Fellowship of the Ring", List.of(author6), "978-0-618-00222-1", 32.99, 4, List.of(location2));
        Book book11 = new Book("Sense and Sensibility", List.of(author7), "978-0-14-143966-2", 26.99, 5, List.of(location3));
        Book book12 = new Book("The Great Gatsby", List.of(author4), "978-0-7432-7356-5", 21.99, 4, List.of(location4));


        author1.setBooks(List.of(book1, book8, book11));
        author2.setBooks(List.of(book2, book8, book12, book1));
        author3.setBooks(List.of(book3, book9, book12));
        author4.setBooks(List.of(book4, book9));
        author5.setBooks(List.of(book5, book10));
        author6.setBooks(List.of(book6, book10));
        author7.setBooks(List.of(book7, book11));

        location1.setBook(book1);
        location2.setBook(book2);
        location3.setBook(book3);
        location4.setBook(book4);

        return List.of(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12);
    }

    @Override
    public Book findByIsbn(String isbn) {
        return findAll().stream().filter(book -> book.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    @Override
    public void save(Book book) {

    }
}
