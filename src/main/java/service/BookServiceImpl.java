package service;

import lombok.extern.slf4j.Slf4j;
import domain.Author;
import domain.Book;
import domain.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class BookServiceImpl implements BookService {

    private Set<Book> books;

    public BookServiceImpl() {
        loadInitBooks();
    }

    public void loadInitBooks() {
        Author author1 = new Author(1L, "George Orwell", null);
        Author author2 = new Author(2L, "Harper Lee", null);
        Author author3 = new Author(3L, "J.D. Salinger", null);
        Author author4 = new Author(4L, "F. Scott Fitzgerald", null);
        Author author5 = new Author(5L, "J.K. Rowling", null);
        Author author6 = new Author(6L, "J.R.R. Tolkien", null);
        Author author7 = new Author(7L, "Jane Austen", null);

        Location location1 = new Location(50, 100, "A", null);
        Location location2 = new Location(200, 300, "B", null);
        Location location3 = new Location(50, 250, "C", null);
        Location location4 = new Location(200, 280, "D", null);

        Book book1 =
                new Book("1984", Set.of(author1, author2), "978-0-452-28423-4", 19.99, 4, Set.of(location1, location2));
        Book book2 = new Book("To Kill a Mockingbird", Set.of(author2), "978-0-06-093546-7", 24.99, 5, Set.of(location2));
        Book book3 = new Book("The Catcher in the Rye", Set.of(author3), "978-0-316-76948-1", 29.99, 3, Set.of(location3));
        Book book5 = new Book("Harry Potter and the Philosopher's Stone", Set.of(author5), "978-0-590-35340-3", 18.99, 5, Set.of(location1));
        Book book6 = new Book("The Hobbit", Set.of(author6), "978-0-618-96864-0", 22.99, 3, Set.of(location2));
        Book book7 = new Book("Pride and Prejudice", Set.of(author7), "978-0-14-143951-8", 25.99, 4, Set.of(location3));
        Book book8 = new Book("Animal Farm", Set.of(author1), "978-0-451-52493-5", 19.99, 5, Set.of(location4));

        Book book10 = new Book("The Lord of the Rings: The Fellowship of the Ring", Set.of(author6), "978-0-618-00222-1", 32.99, 4, Set.of(location2));
        Book book11 = new Book("Sense and Sensibility", Set.of(author7), "978-0-14-143966-2", 26.99, 5, Set.of(location3));
        Book book12 = new Book("The Great Gatsby", Set.of(author4), "978-0-7432-7356-5", 21.99, 4, Set.of(location4));


        author1.setBooks(Set.of(book1, book8, book11));
        author2.setBooks(Set.of(book2, book8, book12, book1));
        author3.setBooks(Set.of(book3, book12));

        author5.setBooks(Set.of(book5, book10));
        author6.setBooks(Set.of(book6, book10));
        author7.setBooks(Set.of(book7, book11));

        location1.setBook(book1);
        location2.setBook(book2);
        location3.setBook(book3);


        this.books = new HashSet<>(Set.of(book1, book2, book3, book5, book6, book7, book8, book10, book11, book12));
    }

    @Override
    public Set<Book> findAll() {
        return books;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return findAll().stream().filter(book -> book.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    @Override
    public void save(Book book) {
        log.info("Saving book: " + book);
        books.add(book);
    }
}
