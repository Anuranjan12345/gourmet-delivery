package com.foodapp.onlinefoodapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/foodapp";

    public static Connection getConnection() throws SQLException {

        String url = System.getenv("FOODEXPRESS_DB_URL");
        String username = System.getenv("FOODEXPRESS_DB_USERNAME");
        String password = System.getenv("FOODEXPRESS_DB_PASSWORD");

        if (url == null || url.isBlank()) {
            url = DEFAULT_URL;
        }

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new SQLException(
                    "Database credentials are missing. "
                    + "Configure FOODEXPRESS_DB_USERNAME and "
                    + "FOODEXPRESS_DB_PASSWORD."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver not found", e
            );
        }

        return DriverManager.getConnection(url, username, password);
    }
}