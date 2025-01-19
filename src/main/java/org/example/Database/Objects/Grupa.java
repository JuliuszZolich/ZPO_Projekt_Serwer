package org.example.Database.Objects;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

/**
 * Klasa reprezentująca grupę w bazie danych.
 */
@Entity
@Setter @Getter
public class Grupa {

    /**
     * Identyfikator grupy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    /**
     * Nazwa grupy.
     */
    @Column(nullable = false)
    private String nazwa;
}