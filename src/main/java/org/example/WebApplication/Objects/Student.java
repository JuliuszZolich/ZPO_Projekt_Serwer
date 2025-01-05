package org.example.WebApplication.Objects;

import java.util.List;

public class Student {
    private String indeks;
    private List<String> ListaObecnosciTermin;
    private List<Integer> ListaObecnosciStatus;

    public List<String> getListaObecnosciTermin()
    {
        return ListaObecnosciTermin;
    }
    public List<Integer> getListaObecnosciStatus()
    {
        return ListaObecnosciStatus;
    }
}
