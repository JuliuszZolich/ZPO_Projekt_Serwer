package org.example;

import org.example.WebApplication.HttpApplication;


public class Main {
    public static void main(String[] args) {

        DatabaseUtils.ConnectToDatabase("database.db"); // Baza danych
        DatabaseUtils.ExecuteUpdate("CREATE TABLE IF NOT EXISTS students (indeks INTEGER PRIMARY KEY, imie TEXT, nazwisko TEXT, grupa INTEGER)");// Tabela użytkowników// Tabela ocen
        //DatabaseUtils.ExecuteUpdate("INSERT INTO students (indeks, imie, nazwisko, grupa) VALUES ('245971','Jan', 'Kowalski', 1)"); // Dodanie przykładowego użytkownika
        HttpApplication.main(new String[]{}); // Strona internetowa dla studenta
        //DatabaseUtils.CloseConnection(); // Zamknięcie połączenia z bazą danych
    }
}
