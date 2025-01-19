package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa pomocnicza reprezentująca obecność w aplikacji webowej.
 */
@Getter @Setter
public class ObecnoscWeb {

    /**
     * Nazwa studenta.
     */
    private String nazwa;

    /**
     * Liczba obecności.
     */
    private int attendance;

    /**
     * Data obecności.
     */
    private String data;
}