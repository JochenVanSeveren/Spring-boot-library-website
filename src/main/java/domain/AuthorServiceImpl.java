package domain;

import model.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorServiceImpl implements AuthorService {
    private List<Author> authors;

    public AuthorServiceImpl() {
        loadInitAuthors();
    }

    private void loadInitAuthors() {
        Author author1 = new Author(1L, "George Orwell", null);
        Author author2 = new Author(2L, "Harper Lee", null);
        Author author3 = new Author(3L, "J.D. Salinger", null);
        Author author4 = new Author(4L, "F. Scott Fitzgerald", null);
        Author author5 = new Author(5L, "J.K. Rowling", null);
        Author author6 = new Author(6L, "J.R.R. Tolkien", null);
        Author author7 = new Author(7L, "Jane Austen", null);

        authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);
        authors.add(author5);
        authors.add(author6);
        authors.add(author7);
    }

    @Override
    public List<Author> findAll() {
        return authors;
    }

    @Override
    public void save(Author author) {
        authors.add(author);
    }

    @Override
    public Author findById(long l) {
        return authors.stream().filter(author -> author.getId() == l).findFirst().orElse(null);
    }

    @Override
    public Author findByName(String authorName) {
        return authors.stream().filter(author -> author.getName().equals(authorName)).findFirst().orElse(null);
    }


}
