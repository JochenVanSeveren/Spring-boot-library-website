package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import validation.ISBN;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"isbn"})
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
    @Range(min = 1, max = 100, message = "{book.price.range}")
    private double price;

    private int stars;
    @OneToMany(mappedBy = "books")
    private Set<Location> locations = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteBooks")
    private Set<User> favoritedByUsers = new HashSet<>();

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

}
