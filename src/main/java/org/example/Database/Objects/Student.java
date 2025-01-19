package org.example.Database.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Klasa reprezentująca studenta w bazie danych.
 */
@Entity
@Getter @Setter
public class Student implements Serializable {

    /**
     * Indeks studenta.
     */
    @Id
    private int indeks;

    /**
     * Imię studenta.
     */
    @Column(nullable = false)
    private String imie;

    /**
     * Nazwisko studenta.
     */
    @Column(nullable = false)
    private String nazwisko;

    /**
     * Identyfikator grupy, do której należy student.
     */
    @Column()
    private Integer grupaId;

    /**
     * Konstruktor domyślny.
     */
    public Student() {
    }
}