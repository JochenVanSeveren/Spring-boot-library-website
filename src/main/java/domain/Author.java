package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    @Id
    private Long id;

    @Getter @Setter private String name;

    @ManyToMany(mappedBy = "authors")
    @Getter @Setter
    private Set<Book> books = new HashSet<>();

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name +
                ", books=" + books +
                '}';
    }

}
