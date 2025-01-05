package org.example.WebApplication.Objects;

public class Login {

    private String indeks;

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public String getIndeks() {
        return indeks;
    }

    public int ParseIndeks() {
        return Integer.parseInt(indeks);
    }
}
