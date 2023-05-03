package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private int plaatscode1;
    private int plaatscode2;
    private String plaatsnaam;

    private Book book;

    @Override
    public String toString() {
        return "Location{" +
                "plaatscode1=" + plaatscode1 +
                ", plaatscode2=" + plaatscode2 +
                ", plaatsnaam='" + plaatsnaam + '\'' +
//                ", book=" + book.getTitle() +
                '}';
    }
}
