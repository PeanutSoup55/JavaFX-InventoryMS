package com.example.javafx_inventoryms.db;

import com.example.javafx_inventoryms.objects.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean addProduct(String name, double price, int quantity, String station){
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

    public static List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); Statement statement = conn.createStatement(); ResultSet set = statement.executeQuery(sql)){

            while (set.next()){
                Product p = new Product(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getDouble("price"),
                    set.getInt("quantity"),
                    set.getString("station"),
                    set.getBoolean("is_empty")
                );
                products.add(p);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    public static boolean updateProduct(int id, String name, double price, int quantity, String station){
        String sql = "UPDATE products SET name=?, price=?, quantity=?, station=?, is_empty=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, station);
            pstmt.setBoolean(5, quantity == 0);
            pstmt.setInt(6, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProduct(int id){
        String sql = "DELETE FROM products WHERE id=?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addSale(double net_revenue, double profit, double payroll_cost){
        String sql = "INSERT INTO sales (net_revenue, profit, payroll_cost) VALUES (?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setDouble(1, net_revenue);
            statement.setDouble(2, profit);
            statement.setDouble(3, payroll_cost);
            statement.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
