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

    public static Connection ConnectToDatabase(String databaseName) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static ResultSet ExecuteQuery(String query) {
        ResultSet rs = null;
        try {
            Statement statement = DatabaseConnection.createStatement();
            statement.setQueryTimeout(SecondsToTimeout);
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}