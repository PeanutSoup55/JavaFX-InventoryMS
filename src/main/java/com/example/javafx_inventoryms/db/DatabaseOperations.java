package com.example.javafx_inventoryms.db;

import com.example.javafx_inventoryms.objects.Product;
import com.example.javafx_inventoryms.objects.Sale;
import com.example.javafx_inventoryms.objects.User;

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
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
    }

    public static boolean addProduct(String name, double price, int quantity, String station){
        String sql = "INSERT INTO products (id, name, price, quantity, station, is_empty) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setString(4, station);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
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
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
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
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    public static boolean deleteProduct(int id){
        String sql = "DELETE FROM products WHERE id=?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
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
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    public static List<Sale> getAllSales(){
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); Statement statement = conn.createStatement(); ResultSet set = statement.executeQuery(sql)){
            while (set.next()){
                Sale s = new Sale(
                    set.getInt("id"),
                    set.getDouble("net_revenue"),
                    set.getDouble("profit"),
                    set.getDouble("payroll_cost"),
                    set.getTimestamp("sale_date")
                );
                sales.add(s);
            }
        }catch (SQLException e){
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
        return sales;
    }

    public static boolean deleteSale(int id){
        String sql = "DELETE FROM sales WHERE id=?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }catch (SQLException e){
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    // User Operations
    public static boolean addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                users.add(u);
            }
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
        return users;
    }

    public static boolean updateUser(int id, String username, String password) {
        String sql = "UPDATE users SET username=?, password=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    public static boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }
}
