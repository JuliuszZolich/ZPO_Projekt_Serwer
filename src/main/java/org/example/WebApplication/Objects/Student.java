package org.example.WebApplication.Objects;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter public class Student {

    private String indeks;
    private String imie;
    private String nazwisko;
    private List<String> ListaObecnosciTermin;
    private List<Integer> ListaObecnosciStatus;

}
