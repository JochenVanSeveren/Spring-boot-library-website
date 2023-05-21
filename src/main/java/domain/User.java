package domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import exception.UserException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"username"})
@ToString(of = {"username"})
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int favoriteLimit = 0;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "Username must be at least 3 characters long and contain only letters and numbers")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private boolean enabled;


//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email address")
//    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Authority> auths = new HashSet<>();


    @ManyToMany
    @JoinTable(
            name = "user_favorite_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @JsonManagedReference
    private Set<Book> favoriteBooks = new HashSet<>();

    public User(int favoriteLimit, String username, String password, Set<Book> favoriteBooks) {
        this.favoriteLimit = favoriteLimit;
        this.username = username;
        this.password = password;
        this.favoriteBooks = favoriteBooks;
        this.enabled = true;
    }

    public void addFavoriteBook(Book book) throws UserException {
        if (this.favoriteBooks == null)
            this.favoriteBooks = new HashSet<>();
        if (this.favoriteBooks.size() >= this.favoriteLimit)
            throw new UserException("The user has reached the limit of favorite books");
        book.setStars(book.getStars() + 1);
        this.favoriteBooks.add(book);
    }

    public void removeFavoriteBook(Book book) throws UserException {
        if (this.favoriteBooks == null)
            this.favoriteBooks = new HashSet<>();
        if (!this.favoriteBooks.contains(book))
            throw new UserException("The user does not have this book in favorites");
        book.setStars(book.getStars() - 1);
        this.favoriteBooks.remove(book);
    }

}
