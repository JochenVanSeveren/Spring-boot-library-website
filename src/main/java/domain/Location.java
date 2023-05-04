package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location implements Serializable {

    @Min(value = 50, message = "plaatscode1 should be greater than or equal to 50")
    @Max(value = 300, message = "plaatscode1 should be less than or equal to 300")
    private int plaatscode1;

    @Min(value = 50, message = "plaatscode2 should be greater than or equal to 50")
    @Max(value = 300, message = "plaatscode2 should be less than or equal to 300")
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

    public void setPlaatscode1(int plaatscode1) {
        if (plaatscode1 < 50 || plaatscode1 > 300) {
            throw new IllegalArgumentException("plaatscode1 should be between 50 and 300");
        }
        this.plaatscode1 = plaatscode1;
    }

    public void setPlaatscode2(int plaatscode2) {
        if (plaatscode2 < 50 || plaatscode2 > 300) {
            throw new IllegalArgumentException("plaatscode2 should be between 50 and 300");
        }
        if (Math.abs(plaatscode2 - plaatscode1) < 50) {
            throw new IllegalArgumentException("plaatscode2 should have a difference of at least 50 from plaatscode1");
        }
        this.plaatscode2 = plaatscode2;
    }

    public void setPlaatsnaam(String plaatsnaam) {
        if (!plaatsnaam.matches("^[a-zA-Z]+$")) {
            throw new IllegalArgumentException("Plaatsnaam should only contain letters");
        }
        this.plaatsnaam = plaatsnaam;
    }

    @Override
    public String toString() {
        return "Location{" +
                "plaatscode1=" + plaatscode1 +
                ", plaatscode2=" + plaatscode2 +
                ", plaatsnaam='" + plaatsnaam + '\'' +
                ", book=" + book +
                '}';
    }
}
