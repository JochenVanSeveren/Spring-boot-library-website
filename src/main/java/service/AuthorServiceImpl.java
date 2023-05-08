package service;

import domain.Author;

import java.util.HashSet;
import java.util.Set;

public class AuthorServiceImpl implements AuthorService {
    private Set<Author> authors = new HashSet<>();

    public AuthorServiceImpl() {
        loadInitAuthors();
    }

    private void loadInitAuthors() {

        Author author1 = new Author(1L, "George Orwell", new HashSet<>());
        Author author2 = new Author(2L, "Harper Lee", new HashSet<>());
        Author author3 = new Author(3L, "J.D. Salinger", new HashSet<>());
        Author author4 = new Author(4L, "F. Scott Fitzgerald", new HashSet<>());
        Author author5 = new Author(5L, "J.K. Rowling", new HashSet<>());
        Author author6 = new Author(6L, "J.R.R. Tolkien", new HashSet<>());
        Author author7 = new Author(7L, "Jane Austen", new HashSet<>());
        Author author8 = new Author(8L, "Gabriel Garcia Marquez", new HashSet<>());
        Author author9 = new Author(9L, "Toni Morrison", new HashSet<>());
        Author author10 = new Author(10L, "Stephen King", new HashSet<>());
        Author author11 = new Author(11L, "Margaret Atwood", new HashSet<>());
        Author author12 = new Author(12L, "Ernest Hemingway", new HashSet<>());
        Author author13 = new Author(13L, "Mark Twain", new HashSet<>());
        Author author15 = new Author(15L, "Charles Dickens", new HashSet<>());
        Author author16 = new Author(16L, "Leo Tolstoy", new HashSet<>());
        Author author17 = new Author(17L, "Victor Hugo", new HashSet<>());
        Author author18 = new Author(18L, "Herman Melville", new HashSet<>());
        Author author19 = new Author(19L, "Oscar Wilde", new HashSet<>());
        Author author20 = new Author(20L, "Virginia Woolf", new HashSet<>());


        authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);
        authors.add(author5);
        authors.add(author6);
        authors.add(author7);
        authors.add(author8);
        authors.add(author9);
        authors.add(author10);
        authors.add(author11);
        authors.add(author12);
        authors.add(author13);
        authors.add(author15);
        authors.add(author16);
        authors.add(author17);
        authors.add(author18);
        authors.add(author19);
        authors.add(author20);

    }

    @Override
    public Set<Author> findAll() {
        return authors;
    }

    @Override
    public void save(Author author) {
        authors.add(author);
    }

//    @Override
//    public Author findById(long l) {
//        return authors.stream().filter(author -> author.getId() == l).findFirst().orElse(null);
//    }

    @Override
    public Author findByName(String authorName) {
        return authors.stream().filter(author -> author.getName().equals(authorName)).findFirst().orElse(null);
    }


}
