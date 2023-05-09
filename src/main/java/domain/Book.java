package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import validation.ISBN;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    public static final int MAX_AUTHORS = 3;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "{book.title.notBlank}")
    private String title;
    @ManyToMany
    private Set<Author> authors = new HashSet<>();
    @ISBN
    private String isbn;

    //    @Min(value = 1, message = "{book.price.min}")
//    @Max(value = 50, message = "{book.price.max}")
    @NotNull
    @Range(min = 1, max = 50, message = "{book.price.range}")
    private double price;

    private int stars;
    @OneToMany(mappedBy = "books")
    private Set<Location> locations = new HashSet<>();

    public Book(String title, Set<Author> authors, String isbn, double price, int stars, Set<Location> locations) {
        this.title = title;
        this.authors = authors;
        this.isbn = isbn;
        this.price = price;
        this.locations = locations;
        this.stars = stars;
    }


    @Override
    public String toString() {
        String authorNames;
        if (authors.isEmpty()) {
            authorNames = "No authors";
        } else {
            authorNames = authors.stream()
                    .map(Author::getName)
                    .collect(Collectors.joining(", "));
        }
        String locationNames;
        if (locations.isEmpty()) {
            locationNames = "No locations";
        } else {
            locationNames = locations.stream()
                    .map(Location::toString)
                    .collect(Collectors.joining(", "));
        }


        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authorNames +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", stars=" + stars +
                ", locations=" + locationNames +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
