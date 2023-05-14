package rest;

import domain.Book;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class BookWebClient {

    private final WebClient client = WebClient.create("http://localhost:8080/api/books");

    public Flux<Book> getBooksByAuthor(String authorName) {
        return client.get()
                .uri("/byAuthor/{authorName}", authorName)
                .retrieve()
                .bodyToFlux(Book.class);
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return client.get()
                .uri("/byIsbn/{isbn}", isbn)
                .retrieve()
                .bodyToMono(Book.class);
    }
}
