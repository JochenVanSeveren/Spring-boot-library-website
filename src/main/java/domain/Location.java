package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import validation.PlaatscodeDifference;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PlaatscodeDifference
public class Location implements Serializable {

    @Id
    private Long id;

    @Range(min = 50, max = 300, message = "{plaatscode1.range}")
    private int plaatscode1;

    @Range(min = 50, max = 300, message = "{plaatscode2.range}")
    private int plaatscode2;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "plaatsnaam should only contain letters")
    private String plaatsnaam;

    @ManyToOne
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return plaatscode1 == location.plaatscode1 && plaatscode2 == location.plaatscode2 && Objects.equals(plaatsnaam, location.plaatsnaam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plaatscode1, plaatscode2, plaatsnaam);
    }

}
