package org.example.Database.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

/**
 * Klasa reprezentująca termin w bazie danych.
 */
@Entity
@Getter @Setter
public class Termin implements Serializable {

    /**
     * Identyfikator terminu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Identyfikator grupy.
     */
    @Column(nullable = false)
    private int grupaId;

    /**
     * Nazwa terminu.
     */
    @Column()
    private String nazwa;

    /**
     * Data terminu.
     */
    @Column(nullable = false)
    private String data;

    /**
     * Identyfikator prowadzącego.
     */
    @Column(nullable = false)
    private int prowadzacyId;
}