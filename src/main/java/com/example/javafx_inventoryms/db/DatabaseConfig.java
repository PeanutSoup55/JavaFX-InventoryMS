package com.example.javafx_inventoryms.db;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/InvetoryMS";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    public static String getUrl() {
        return DB_URL;
    }

    public static String getUsername() {
        return DB_USERNAME;
    }

    public static String getPassword() {
        return DB_PASSWORD;
    }
}