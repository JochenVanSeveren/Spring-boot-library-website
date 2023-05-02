package domain;

import model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findByIsbn(String isbn);

    void save(Book book);

 }
