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

        authors = new HashSet<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);
        authors.add(author5);
        authors.add(author6);
        authors.add(author7);
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
