package com.example.javafx_inventoryms.db;

import java.sql.*;

public class DatabaseOperations {

    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUsername();
    private static final String PASS = DatabaseConfig.getPassword();

    public void Initialize(){
        String usersSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(50) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL"
                + ");";

        // 2. Updated Products Table (Now with station and empty)
        String productsSQL = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "price DECIMAL(10, 2), "
                + "quantity INT, "
                + "station VARCHAR(50), "
                + "is_empty BOOLEAN DEFAULT FALSE"
                + ");";

        // 3. Sales Table
        String salesSQL = "CREATE TABLE IF NOT EXISTS sales ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "net_revenue DECIMAL(10, 2), "
                + "profit DECIMAL(10, 2), "
                + "payroll_cost DECIMAL(10, 2), "
                + "sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); Statement statement = conn.createStatement()){
            statement.executeUpdate(usersSQL);
            statement.executeUpdate(productsSQL);
            statement.executeUpdate(salesSQL);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean addProduct(int id, String name, double price, int quantity, String station){
        String sql = "INSERT INTO products (id, name, price, quantity, station, is_empty) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setString(4, station);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
