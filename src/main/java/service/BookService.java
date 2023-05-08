package service;

import domain.Book;

import java.util.Set;

public interface BookService {

    Set<Book> findAll();

    Book findByIsbn(String isbn);

    void save(Book book);

}
