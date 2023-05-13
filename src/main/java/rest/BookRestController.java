package rest;

import domain.Author;
import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import repository.AuthorRepository;
import repository.BookRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/byAuthor/{authorName}")
    public Set<Book> getBooksByAuthor(@PathVariable String authorName) {
        Author author = authorRepository.findByName(authorName);
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        return new HashSet<>(author.getBooks());
    }

    @GetMapping("/byIsbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }
}
