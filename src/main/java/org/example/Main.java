package org.example;

import org.example.WebApplication.HttpApplication;


public class Main {
    public static void main(String[] args) {

        DatabaseUtils.ConnectToDatabase("mydb.db"); // Baza danych
        DatabaseUtils.FillDatabase();
        HttpApplication.main(new String[]{}); // Strona internetowa dla studenta
        //DatabaseUtils.CloseConnection(); // Zamknięcie połączenia z bazą danych
    }
}
