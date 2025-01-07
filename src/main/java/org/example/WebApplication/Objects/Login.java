package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter public class Login {

    private String indeks;

    public int ParseIndeks() {
        return Integer.parseInt(indeks);
    }
}
