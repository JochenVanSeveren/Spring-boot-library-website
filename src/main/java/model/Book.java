package model;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @Data
public class Book {

    private String title;
    private List<Author> authors;
    private String isbn;
    private double price;
    private int starts;
    private List<Location> locations;

//    @ManyToMany(mappedBy = "favorites")
//    private List<User> favoritedByUsers;

    public static final int MAX_AUTHORS = 3;


}
