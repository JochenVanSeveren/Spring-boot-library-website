package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"username"})
@ToString(of = {"username", "role"})
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String role;
    private boolean isFavoriteLimitReached;

    @NotBlank(message = "Username is required")
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "Username must be at least 3 characters long and contain only letters and numbers")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email address")
//    private String email;

    @ManyToMany
    @JoinTable(
            name = "user_favorite_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> favoriteBooks = new HashSet<>();

//    public User(String role, boolean b) {
//        this.role = role;
//        isFavoriteLimitReached = b;
//    }

    public User(String role, boolean isFavoriteLimitReached, String username, String password, Set<Book> favoriteBooks) {
        this.role = role;
        this.isFavoriteLimitReached = isFavoriteLimitReached;
        this.username = username;
        this.password = password;
        this.favoriteBooks = favoriteBooks;
    }
}
