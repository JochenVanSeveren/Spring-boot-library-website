package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Author implements Serializable {

    @Id
    private Long id;

    @Getter
    @Setter
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "authors")
    @Getter
    @Setter
    private Set<Book> books = new HashSet<>();

    @Override
    public String toString() {
        String bookTitles;
        if (books.isEmpty()) {
            bookTitles = "No books";
        } else {
            bookTitles = books.stream()
                    .map(Book::getTitle)
                    .collect(Collectors.joining(", "));
        }

        return "Author{" +
                "name='" + name + '\'' +
                ", books=" + bookTitles +
                '}';
    }
}
