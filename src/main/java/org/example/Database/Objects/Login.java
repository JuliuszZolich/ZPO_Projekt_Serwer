package org.example.Database.Objects;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Setter @Getter
public class Login {
    @Id
    private String indeks;

    public int parseIndeks() {
        return Integer.parseInt(indeks);
    }
}
