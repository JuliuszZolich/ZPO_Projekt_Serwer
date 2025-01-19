package org.example.Database.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Klasa reprezentująca obecność w bazie danych.
 */
@Entity
@Getter @Setter
public class Obecnosc implements Serializable {

    /**
     * Identyfikator obecności.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Liczba obecności.
     */
    @Column(nullable = false)
    private int attendance;

    /**
     * Identyfikator studenta.
     */
    @Column(nullable = false)
    private int studentId;

    /**
     * Identyfikator terminu.
     */
    @Column(nullable = false)
    private int terminId;
}