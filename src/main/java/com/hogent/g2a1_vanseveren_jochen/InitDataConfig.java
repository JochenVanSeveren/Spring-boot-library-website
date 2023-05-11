package com.hogent.g2a1_vanseveren_jochen;

import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.Arrays;
import java.util.Set;

@Component
public class InitDataConfig implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;


    @Override
    public void run(String... args) {

        Author author1 = new Author("George Orwell", null);
        Author author2 = new Author("Harper Lee", null);
        Author author3 = new Author("J.D. Salinger", null);
        Author author4 = new Author("F. Scott Fitzgerald", null);
        Author author5 = new Author("J.K. Rowling", null);
        Author author6 = new Author("J.R.R. Tolkien", null);
        Author author7 = new Author("Jane Austen", null);
        Author author8 = new Author("Gabriel Garcia Marquez", null);
        Author author9 = new Author("Toni Morrison", null);
        Author author10 = new Author("Stephen King", null);
        Author author11 = new Author("Margaret Atwood", null);
        Author author12 = new Author("Ernest Hemingway", null);
        Author author13 = new Author("Mark Twain", null);
        Author author15 = new Author("Charles Dickens", null);
        Author author16 = new Author("Leo Tolstoy", null);
        Author author17 = new Author("Victor Hugo", null);
        Author author18 = new Author("Herman Melville", null);
        Author author19 = new Author("Oscar Wilde", null);
        Author author20 = new Author("Virginia Woolf", null);

        Location location1 = new Location(50, 100, "A", null);
        Location location2 = new Location(200, 300, "B", null);
        Location location3 = new Location(50, 250, "C", null);
        Location location4 = new Location(200, 280, "D", null);
        Location location5 = new Location(150, 200, "E", null);
        Location location6 = new Location(250, 200, "F", null);
        Location location7 = new Location(100, 300, "G", null);
        Location location8 = new Location(300, 100, "H", null);
        Location location9 = new Location(150, 250, "I", null);


        authorRepository.saveAll(Arrays.asList(author1, author2, author3, author4, author5, author6, author7, author8, author9, author10, author11, author12, author13, author15, author16, author17, author18, author19, author20));

        Book book1 = new Book("1984", Set.of(author1, author2), "978-0-452-28423-4", 19.99, 4, Set.of(location1, location2));
        Book book2 = new Book("To Kill a Mockingbird", Set.of(author2), "978-0-06-093546-7", 24.99, 5, Set.of(location2));
        Book book3 = new Book("The Catcher in the Rye", Set.of(author3), "978-0-316-76917-4", 29.99, 3, Set.of(location3));
        Book book4 = new Book("One Hundred Years of Solitude", Set.of(author8), "978-0-06-088328-7", 17.99, 4, Set.of(location4));
        Book book5 = new Book("Harry Potter and the Philosopher's Stone", Set.of(author5), "978-0-590-35340-3", 18.99, 5, Set.of(location1));
        Book book6 = new Book("The Hobbit", Set.of(author6), "978-0-547-92822-7", 22.99, 3, Set.of(location2));
        Book book7 = new Book("Pride and Prejudice", Set.of(author7), "978-0-14-143951-8", 25.99, 4, Set.of(location3));
        Book book8 = new Book("Animal Farm", Set.of(author1), "978-0-451-52493-5", 19.99, 5, Set.of(location4));
        Book book9 = new Book("Beloved", Set.of(author9), "978-1-400-03341-6", 23.99, 5, Set.of(location5));
        Book book10 = new Book("The Lord of the Rings: The Fellowship of the Ring", Set.of(author6), "978-0-618-00222-1", 32.99, 4, Set.of(location2));
        Book book11 = new Book("Sense and Sensibility", Set.of(author7), "978-0-14-143966-2", 26.99, 5, Set.of(location3));
        Book book12 = new Book("The Great Gatsby", Set.of(author4), "978-0-7432-7356-5", 21.99, 4, Set.of(location4));
        Book book13 = new Book("The Shining", Set.of(author10), "978-0-385-12167-5", 16.99, 4, Set.of(location6));
        Book book14 = new Book("The Handmaid's Tale", Set.of(author11), "978-0-385-49081-8", 28.99, 5, Set.of(location7));
        Book book15 = new Book("The Old Man and the Sea", Set.of(author12), "978-0-684-80122-3", 14.99, 4, Set.of(location8));
        Book book16 = new Book("Adventures of Huckleberry Finn", Set.of(author13), "978-1-953-64980-5", 15.99, 5, Set.of(location9));
        Book book18 = new Book("A Tale of Two Cities", Set.of(author15), "978-0-451-53057-8", 12.99, 5, Set.of(location1));
        Book book19 = new Book("War and Peace", Set.of(author16), " 978-1-400-07998-8", 37.99, 4, Set.of(location2));
        Book book20 = new Book("Les Miserables", Set.of(author17), "978-0-451-41943-9", 20.99, 5, Set.of(location3));

        User user1 = new User("admin", false, "adminUser", "123456789", null);
        User user2 = new User("user", false, "user", "123456789", null);
        User user3 = new User("user1", false, "Jochen", "123456789", null);
        User user4 = new User("user2", false, "user2", "123456789", null);
        User user5 = new User("user3", false, "user3", "123456789", null);

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12, book13, book14, book15, book16, book18, book19, book20));
        locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9));


        author7.setBooks(Set.of(book7, book11));
        author8.setBooks(Set.of(book4));
        author9.setBooks(Set.of(book9));
        author10.setBooks(Set.of(book13));
        author11.setBooks(Set.of(book14));
        author12.setBooks(Set.of(book15));
        author13.setBooks(Set.of(book16));
        author15.setBooks(Set.of(book18));
        author16.setBooks(Set.of(book19));
        author17.setBooks(Set.of(book20));
        author18.setBooks(Set.of(book15, book16, book19));
        author19.setBooks(Set.of(book4, book5, book7, book11));
        author20.setBooks(Set.of(book14, book18));
        author7.setBooks(Set.of(book7, book11));
        author8.setBooks(Set.of(book4));
        author9.setBooks(Set.of(book9));
        author10.setBooks(Set.of(book13));
        author11.setBooks(Set.of(book14));
        author12.setBooks(Set.of(book15));
        author13.setBooks(Set.of(book16));
        author15.setBooks(Set.of(book18));
        author16.setBooks(Set.of(book19));
        author17.setBooks(Set.of(book20));
        author18.setBooks(Set.of(book15, book16, book19));
        author19.setBooks(Set.of(book4, book5, book7, book11));
        author20.setBooks(Set.of(book14, book18));

        location1.setBook(book1);
        location2.setBook(book2);
        location2.setBook(book6);
        location2.setBook(book10);
        location2.setBook(book19);
        location3.setBook(book3);
        location3.setBook(book7);
        location3.setBook(book11);
        location3.setBook(book20);
        location4.setBook(book8);
        location4.setBook(book12);
        location5.setBook(book9);
        location6.setBook(book13);
        location7.setBook(book14);
        location8.setBook(book15);
        location9.setBook(book16);


        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12, book13, book14, book15, book16, book18, book19, book20));
        locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9));
        authorRepository.saveAll(Arrays.asList(author1, author2, author3, author4, author5, author6, author7, author8, author9, author10, author11, author12, author13, author15, author16, author17, author18, author19, author20));




    }

}
