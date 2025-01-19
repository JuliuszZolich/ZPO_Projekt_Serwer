package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca błąd w aplikacji.
 */
@Getter @Setter
public class Error {

    /**
     * Wiadomość błędu.
     */
    private String error;

    /**
     * Konstruktor klasy Error.
     *
     * @param error Wiadomość błędu.
     */
    public Error(String error) {
        this.error = error;
    }
}