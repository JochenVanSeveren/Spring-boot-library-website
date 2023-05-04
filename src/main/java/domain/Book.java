package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validation.ISBN;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @ManyToMany
    @Size(min = 1, max = MAX_AUTHORS, message = "There must be between 1 and 3 authors")
    private Set<Author> authors = new HashSet<>();

    @ISBN
    private String isbn;

    @Min(value = 1, message = "Price must be greater than 0")
    @Max(value = 100, message = "Price must be less than 100")
    private double price;

    private int stars;

    @OneToMany(mappedBy = "books")
    private Set<Location> locations = new HashSet<>();

    public static final int MAX_AUTHORS = 3;

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
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", stars=" + stars +
                ", locations=" + locations +
                '}';
    }
}
