package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
