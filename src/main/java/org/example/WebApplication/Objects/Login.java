package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

public class Login {

    @Getter @Setter private String indeks;

    public int ParseIndeks() {
        return Integer.parseInt(indeks);
    }
}
