package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class Author implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotNull
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @Getter
    @Setter
    private Set<Book> books = new HashSet<>();


    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", books=" + getBookTitles() +
                '}';
    }

    public String getBookTitles() {
        if (books == null || books.isEmpty()) {
            return "No books";
        } else {
            return books.stream()
                    .map(Book::getTitle)
                    .collect(Collectors.joining(", "));
        }
    }
}
