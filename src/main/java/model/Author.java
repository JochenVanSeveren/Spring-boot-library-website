package model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    @Id
    private Long id;

    private String name;

    private List<Book> books;

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name +
                ", books=" + books +
                '}';
    }

}
