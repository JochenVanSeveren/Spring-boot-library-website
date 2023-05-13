package perform;

import rest.BookWebClient;

public class PerformRestExample {

    public PerformRestExample() {
        BookWebClient client = new BookWebClient();

        // Get books by an author
        client.getBooksByAuthor("Test Author")
                .subscribe(book -> System.out.println("Book title: " + book.getTitle()));

        // Get a book by ISBN
        client.getBookByIsbn("1234567890123")
                .subscribe(book -> System.out.println("Book title: " + book.getTitle()));
    }


}
