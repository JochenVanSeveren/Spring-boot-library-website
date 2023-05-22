package domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Range;
import validation.ISBN;

import java.io.Serial;
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
    public static final int MAX_LOCATIONS = 3;

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "{book.title.notBlank}")
    private String title;

    @JsonBackReference
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Author> authors;

    @ISBN
    @Getter
    @Setter
    @Column(unique = true)
    private String isbn;

    @Range(min = 1, max = 100, message = "{book.price.range}")
    private double price;

    @Formula("(SELECT COUNT(*) FROM user_favorite_books WHERE user_favorite_books.book_id = id)")
    private int stars;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Location> locations = new HashSet<>();
//
//    @ManyToMany(mappedBy = "favoriteBooks")
//    @JsonManagedReference
//    private Set<User> favoritedByUsers = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteBooks")
    @JsonBackReference
    private Set<User> users = new HashSet<>();


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
        if (authors == null || authors.isEmpty()) {
            authorNames = "No authors";
        } else {
            authorNames = authors.stream()
                    .map(Author::getName)
                    .collect(Collectors.joining(", "));
        }
        String locationNames;
        if (locations == null || locations.isEmpty()) {
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

    public void removeLocation(Location location) {
        locations.remove(location);
        location.setBook(null);
    }
}
