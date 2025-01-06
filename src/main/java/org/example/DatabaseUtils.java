package org.example;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils
{
    public static Connection DatabaseConnection;
    @Getter @Setter private static int SecondsToTimeout = 30;

    public static void ConnectToDatabase(String databaseName) {
        try {
            DatabaseConnection =  DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet ExecuteQuery(String query) {
        try {
            Statement statement = DatabaseConnection.createStatement();
            statement.setQueryTimeout(SecondsToTimeout);
            statement.closeOnCompletion();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int ExecuteUpdate(String query) {
        try {
            Statement statement = DatabaseConnection.createStatement();
            statement.setQueryTimeout(SecondsToTimeout);
            statement.closeOnCompletion();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void CloseConnection() {
        try {
            System.out.println("Closing connection");
            DatabaseConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void FillDatabase(){
        ExecuteUpdate("""
                CREATE TABLE Student (
                id INTEGER PRIMARY KEY,
                indeks INTEGER NOT NULL,
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                grupa_id INTEGER,
                haslo VARCHAR NOT NULL
                );
        """);
        ExecuteUpdate("""
                CREATE TABLE Prowadzacy (
                id INTEGER PRIMARY KEY,
                imie VARCHAR NOT NULL,
                nazwisko VARCHAR NOT NULL,
                haslo VARCHAR NOT NULL
                );
        """);

        ExecuteUpdate("""
                CREATE TABLE Obecnosci (
                id INTEGER PRIMARY KEY,
                student_id INTEGER NOT NULL,
                attendance INTEGER NOT NULL,
                termin_id INTEGER NOT NULL,
                FOREIGN KEY (student_id) REFERENCES Student (id),
                FOREIGN KEY (termin_id) REFERENCES Terminy (id)
        );
        """);

        ExecuteUpdate("""
                CREATE TABLE Terminy (
                id INTEGER PRIMARY KEY,
                grupa_id INTEGER NOT NULL,
                nazwa VARCHAR NOT NULL,
                data DATE NOT NULL,
                prowadzacy_id INTEGER NOT NULL,
                FOREIGN KEY (prowadzacy_id) REFERENCES Prowadzacy (id)
                );
        """);
    }
}