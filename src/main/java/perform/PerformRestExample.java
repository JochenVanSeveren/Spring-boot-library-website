package perform;

import org.springframework.web.reactive.function.client.WebClient;

public class PerformRestExample {

    private final String SERVER_URI = "http://localhost:8080/api/books";
    private final WebClient webClient = WebClient.create();


    public PerformRestExample() {
        getBooksByAuthor("George Orwell");
        getBookByIsbn("978-0-452-28423-4");
    }

    private void getBookByIsbn(String isbn) {
        webClient.get()
                .uri(SERVER_URI + "/byIsbn/" + isbn)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
    }

    private void getBooksByAuthor(String authorName) {
        webClient.get()
                .uri(SERVER_URI + "/byAuthor/" + authorName)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(System.out::println);
    }


}
