package model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.validation.ISBN;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Size(min = 1, max = MAX_AUTHORS, message = "There must be between 1 and 3 authors")
    private List<Author> authors;

    @ISBN
    private String isbn;

    @Min(value = 1, message = "Price must be greater than 0")
    @Max(value = 100, message = "Price must be less than 100")
    private double price;

    private int stars;

    private List<Location> locations;

    public static final int MAX_AUTHORS = 3;


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
