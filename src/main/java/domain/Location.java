package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import validation.PlaatscodeDifference;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PlaatscodeDifference
@EqualsAndHashCode(of = {"plaatscode1", "plaatscode2", "plaatsnaam"})
public class Location implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Range(min = 50, max = 300, message = "{plaatscode1.range}")
    private int plaatscode1;

    @Range(min = 50, max = 300, message = "{plaatscode2.range}")
    private int plaatscode2;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "plaatsnaam should only contain letters")
    private String plaatsnaam;


    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    public Location(int plaatscode1, int plaatscode2, String plaatsnaam, Book book) {
        setPlaatscode1(plaatscode1);
        setPlaatscode2(plaatscode2);
        setPlaatsnaam(plaatsnaam);
        this.book = book;
    }

    @Override
    public String toString() {
        if (book == null) return "Location{" +
                "plaatscode1=" + plaatscode1 +
                ", plaatscode2=" + plaatscode2 +
                ", plaatsnaam='" + plaatsnaam + '\'' +
                ", book=null" +
                '}';
        return "Location{" +
                "plaatscode1=" + plaatscode1 +
                ", plaatscode2=" + plaatscode2 +
                ", plaatsnaam='" + plaatsnaam + '\'' +
                ", book=" + book.getTitle() +
                '}';
    }

}
