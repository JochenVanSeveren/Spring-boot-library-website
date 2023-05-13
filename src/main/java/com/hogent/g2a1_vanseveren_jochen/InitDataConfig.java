package com.hogent.g2a1_vanseveren_jochen;

import domain.Author;
import domain.Book;
import domain.Location;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.LocationRepository;
import repository.UserRepository;

import java.util.Arrays;
import java.util.Set;

@Component
public class InitDataConfig implements CommandLineRunner {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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
        Author author21 = new Author("Toni Morrison", null);

        Location location1 = new Location(50, 100, "A", null);
        Location location2 = new Location(200, 300, "B", null);
        Location location3 = new Location(50, 250, "C", null);
        Location location4 = new Location(200, 150, "D", null);
        Location location5 = new Location(150, 200, "E", null);
        Location location6 = new Location(250, 200, "F", null);
        Location location7 = new Location(100, 300, "G", null);
        Location location8 = new Location(300, 100, "H", null);
        Location location9 = new Location(150, 250, "I", null);
        Location location10 = new Location(250, 150, "J", null);
        Location location11 = new Location(100, 200, "K", null);
        Location location12 = new Location(300, 200, "L", null);
        Location location13 = new Location(50, 100, "M", null);
        Location location14 = new Location(200, 300, "N", null);
        Location location15 = new Location(50, 250, "O", null);
        Location location16 = new Location(200, 300, "P", null);
        Location location17 = new Location(150, 200, "Q", null);
        Location location18 = new Location(250, 200, "R", null);
        Location location19 = new Location(100, 300, "S", null);
        Location location20 = new Location(300, 100, "T", null);
        Location location21 = new Location(150, 250, "U", null);
        Location location22 = new Location(250, 300, "V", null);
        Location location23 = new Location(100, 200, "W", null);
        Location location24 = new Location(300, 200, "X", null);
        Location location25 = new Location(50, 100, "Y", null);
        Location location26 = new Location(200, 300, "Z", null);
        Location location27 = new Location(50, 250, "AA", null);
        Location location28 = new Location(200, 280, "AB", null);
        Location location29 = new Location(150, 200, "AC", null);
        Location location30 = new Location(250, 200, "AD", null);
        Location location31 = new Location(100, 300, "AE", null);
        Location location32 = new Location(300, 100, "AF", null);
        Location location33 = new Location(150, 250, "AG", null);
        Location location34 = new Location(250, 300, "AH", null);



        authorRepository.saveAll(Arrays.asList(author1, author2, author3, author4, author5, author6, author7, author8, author9, author10, author11, author12, author13, author15, author16, author17, author18, author19, author20, author21));

        Book book1 = new Book("1984", Set.of(author1), "978-0-452-28423-4", 19.99, 4, Set.of(location1, location2));
        Book book2 = new Book("To Kill a Mockingbird", Set.of(author2), "978-0-06-093546-7", 24.99, 5, Set.of(location3, location4));
        Book book3 = new Book("The Catcher in the Rye", Set.of(author3), "978-0-316-76917-4", 29.99, 3, Set.of(location5, location6));
        Book book4 = new Book("The Great Gatsby", Set.of(author4), "978-0-7432-7356-5", 21.99, 4, Set.of(location7));
        Book book5 = new Book("Harry Potter and the Philosopher's Stone", Set.of(author5), "978-0-590-35340-3", 18.99, 5, Set.of(location8, location9));
        Book book6 = new Book("The Hobbit", Set.of(author6), "978-0-547-92822-7", 22.99, 3, Set.of(location10, location11, location12));
        Book book7 = new Book("Pride and Prejudice", Set.of(author7), "978-0-14-143951-8", 25.99, 4, Set.of(location13, location14));
        Book book8 = new Book("Animal Farm", Set.of(author1), "978-0-451-52493-5", 19.99, 5, Set.of(location15));
        Book book9 = new Book("One Hundred Years of Solitude", Set.of(author8), "978-0-06-088328-7", 17.99, 4, Set.of(location16, location17, location18));
        Book book10 = new Book("Beloved", Set.of(author21), "978-1-400-03341-6", 23.99, 5, Set.of(location19));
        Book book11 = new Book("The Lord of the Rings: The Fellowship of the Ring", Set.of(author6), "978-0-618-00222-1", 32.99, 4, Set.of(location20, location21));
        Book book12 = new Book("Sense and Sensibility", Set.of(author7), "978-0-14-143966-2", 26.99, 5, Set.of(location22, location23));
        Book book13 = new Book("The Shining", Set.of(author10), "978-0-385-12167-5", 16.99, 4, Set.of(location24));
        Book book14 = new Book("The Handmaid's Tale", Set.of(author11), "978-0-385-49081-8", 28.99, 5, Set.of(location25, location26));
        Book book15 = new Book("The Old Man and the Sea", Set.of(author12), "978-0-684-80122-3", 14.99, 4, Set.of(location27));
        Book book16 = new Book("Adventures of Huckleberry Finn", Set.of(author13), "978-1-953-64980-5", 15.99, 5, Set.of(location28));
        Book book18 = new Book("A Tale of Two Cities", Set.of(author15), "978-0-451-53057-8", 12.99, 5, Set.of(location29,location30));
        Book book19 = new Book("War and Peace", Set.of(author16), " 978-1-400-07998-8", 37.99, 4, Set.of(location31, location32, location33));
        Book book20 = new Book("Les Miserables", Set.of(author17), "978-0-451-41943-9", 20.99, 5, Set.of(location34));

        User user1 = new User("ADMIN", 5, "admin", passwordEncoder.encode( "admin"), null);
        User user2 = new User("USER", 6, "user", passwordEncoder.encode( "user"), null);
        User user3 = new User("USER", 7, "Jochen", passwordEncoder.encode( "123456789"), null);
        User user4 = new User("USER", 8, "user2", passwordEncoder.encode( "123456789"), null);
        User user5 = new User("USER", 9, "user3", passwordEncoder.encode( "123456789"), null);

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12, book13, book14, book15, book16, book18, book19, book20));
        locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9));

        author1.setBooks(Set.of(book1, book8));
        author2.setBooks(Set.of(book2));
        author3.setBooks(Set.of(book3));
        author4.setBooks(Set.of(book4));
        author5.setBooks(Set.of(book5));
        author6.setBooks(Set.of(book6, book11));
        author7.setBooks(Set.of(book7, book12));
        author8.setBooks(Set.of(book9));
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
        author21.setBooks(Set.of(book10));


        location1.setBook(book1);
        location2.setBook(book1);
        location3.setBook(book2);
        location4.setBook(book2);
        location5.setBook(book3);
        location6.setBook(book3);
        location7.setBook(book4);
        location8.setBook(book5);
        location9.setBook(book5);
        location10.setBook(book6);
        location11.setBook(book6);
        location12.setBook(book6);
        location13.setBook(book7);
        location14.setBook(book7);
        location15.setBook(book8);
        location16.setBook(book9);
        location17.setBook(book9);
        location18.setBook(book9);
        location19.setBook(book10);
        location20.setBook(book11);
        location21.setBook(book11);
        location22.setBook(book12);
        location23.setBook(book12);
        location24.setBook(book13);
        location25.setBook(book14);
        location26.setBook(book14);
        location27.setBook(book15);
        location28.setBook(book16);
        location29.setBook(book18);
        location30.setBook(book18);
        location31.setBook(book19);
        location32.setBook(book19);
        location33.setBook(book19);
        location34.setBook(book20);

        try {
            user1.addFavoriteBook(book1);
            user1.addFavoriteBook(book2);
            user1.addFavoriteBook(book3);
            user1.addFavoriteBook(book4);

            user2.addFavoriteBook(book5);
            user2.addFavoriteBook(book6);
            user2.addFavoriteBook(book7);
            user2.addFavoriteBook(book8);
            user2.addFavoriteBook(book9);

            user3.addFavoriteBook(book1);
            user3.addFavoriteBook(book3);
            user3.addFavoriteBook(book10);
            user3.addFavoriteBook(book11);

            user4.addFavoriteBook(book2);
            user4.addFavoriteBook(book1);
            user4.addFavoriteBook(book12);

            user5.addFavoriteBook(book2);
            user5.addFavoriteBook(book3);
            user5.addFavoriteBook(book4);
            user5.addFavoriteBook(book5);

            user1.addFavoriteBook(book19);
            user2.addFavoriteBook(book19);
            user3.addFavoriteBook(book19);
            user4.addFavoriteBook(book19);
            user5.addFavoriteBook(book19);
        }
        catch (Exception e){
            e.printStackTrace();
        }




        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10, book11, book12, book13, book14, book15, book16, book18, book19, book20));
        locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5, location6, location7, location8, location9, location10, location11, location12, location13, location14, location15, location16, location17, location18, location19, location20, location21, location22, location23, location24, location25, location26, location27, location28, location29, location30, location31, location32, location33, location34));
        authorRepository.saveAll(Arrays.asList(author1, author2, author3, author4, author5, author6, author7, author8, author9, author10, author11, author12, author13, author15, author16, author17, author18, author19, author20, author21));


    }

}
