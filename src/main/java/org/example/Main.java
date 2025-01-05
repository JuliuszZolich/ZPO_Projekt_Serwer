package org.example;

import org.example.models.ConnectDB;


import java.sql.*;


public class Main {

    public static void main(String[] args) {

        ConnectDB db = new ConnectDB();

        Connection connection = db.getConnection();


        if (connection != null) {

            try {

                Statement statement = connection.createStatement();

                String createTable="CREATE TABLE IF NOT EXISTS students (id INTEGER PRIMARY KEY, name TEXT)";

                // Create table if not exists

//                Operation:C

                statement.executeUpdate(createTable);


                // Insert data

                statement.executeUpdate("INSERT INTO students (name) VALUES (‘John’)");

                statement.executeUpdate("INSERT INTO students (name) VALUES (‘Alice’)");

                System.out.println("Data Inserted");


//                Operation:R

                // Display data

                ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

                System.out.println("ID\tName");

                while (resultSet.next()) {

                    int id = resultSet.getInt("id");

                    String name = resultSet.getString("name");

                    System.out.println(id + "\t" + name);

                }


//                Operation:U

                // Update data

                statement.executeUpdate("UPDATE students SET name = ‘Johnny’ WHERE id = 1");

                System.out.println("Data Updated");

                // Display updated data

                resultSet = statement.executeQuery("SELECT * FROM students");

                System.out.println("ID\tName");

                while (resultSet.next()) {

                    int id = resultSet.getInt("id");

                    String name = resultSet.getString("name");

                    System.out.println(id + "\t" + name);

                }


//                Operation:D

//                 Delete data

                statement.executeUpdate("DELETE FROM students WHERE id = 2");


                // Close resources

                resultSet.close();

                statement.close();

            } catch (SQLException e) {

                e.printStackTrace();

            } finally {

                db.closeConnection();

            }

        } else {

            System.out.println("Connection failed.");

        }

    }

}