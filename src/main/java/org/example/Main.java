package org.example;

import org.example.WebApplication.HttpApplication;


public class Main {
    public static void main(String[] args) {

        //TODO Encje (podjebać z prezentacji z wykładu)

        //DatabaseUtils.ConnectToDatabase("mydb.db"); // Baza danych
        //DatabaseUtils.FillDatabase();
        //DatabaseUtils.CloseConnection(); // Zamknięcie połączenia z bazą danych
        HttpApplication.main(new String[]{}); // Strona internetowa dla studenta
    }
}
