package org.util;

import java.sql.*;

public class PsqlConnector {
    public static void main(String[] args) throws ClassNotFoundException {
        try (final Connection connection =
                     DriverManager.getConnection("jdbc:postgresql://postgis-ramisohj.f.aivencloud.com:26882/defaultdb?ssl=require&user=user_name&password=pass_here");
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT * FROM bol_adm3")) {

            // Get metadata from the ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Get the number of columns
            int columnCount = metaData.getColumnCount();

            // Print column names
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t"); // Print column name
            }
            System.out.println(); // Move to the next line

            // Print rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount-1; i++) {
                    System.out.print(resultSet.getString(i) + "\t"); // Print column value
                }
                System.out.println(); // Move to the next line
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}