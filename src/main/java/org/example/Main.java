package org.example;

import org.example.WebApplication.ZpoProjektSerwerApplication;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DatabaseUtils.ConnectToDatabase("database.db"); // Baza danych
        DatabaseUtils.ExecuteUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, role TEXT)"); // Tabela użytkowników
        DatabaseUtils.ExecuteUpdate("CREATE TABLE IF NOT EXISTS subjects (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, teacher TEXT)"); // Tabela przedmiotów
        DatabaseUtils.ExecuteUpdate("CREATE TABLE IF NOT EXISTS grades (id INTEGER PRIMARY KEY AUTOINCREMENT, subject_id INTEGER, student_id INTEGER, grade INTEGER, date TEXT)"); // Tabela ocen

        ZpoProjektSerwerApplication.main(new String[]{}); // Strona internetowa dla studenta
    }
}
