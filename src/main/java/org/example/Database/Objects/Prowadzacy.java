package org.example.Database.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Klasa reprezentująca prowadzącego w bazie danych.
 */
@Entity
@Getter @Setter
public class Prowadzacy implements Serializable {

    /**
     * Identyfikator prowadzącego.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Imię prowadzącego.
     */
    @Column(nullable = false)
    private String imie;

    /**
     * Nazwisko prowadzącego.
     */
    @Column(nullable = false)
    private String nazwisko;

    /**
     * Hasło prowadzącego.
     */
    @Column(nullable = false)
    private String haslo;
}