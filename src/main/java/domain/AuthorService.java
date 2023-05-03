package domain;

import model.Author;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    void save(Author author);

    Author findById(long l);



    Author findByName(String authorName);
}
