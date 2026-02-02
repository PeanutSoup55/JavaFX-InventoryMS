package com.example.javafx_inventoryms.db;

import com.example.javafx_inventoryms.objects.Product;
import com.example.javafx_inventoryms.objects.Sale;
import com.example.javafx_inventoryms.objects.SaleItem;
import com.example.javafx_inventoryms.objects.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseOperations {

    private static final String URL = DatabaseConfig.getUrl();
    private static final String USER = DatabaseConfig.getUsername();
    private static final String PASS = DatabaseConfig.getPassword();

    public static void Initialize(){
        String usersSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(50) UNIQUE NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "position VARCHAR(255) NOT NULL, "
                + "pay DOUBLE(10, 2) NOT NULL"
                + ");";

        String productsSQL = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "price DECIMAL(10, 2), "
                + "quantity INT, "
                + "cog DOUBLE(10, 2), "
                + "station VARCHAR(50), "
                + "is_empty BOOLEAN DEFAULT FALSE"
                + ");";

        // 3. Sales Table
        String salesSQL = "CREATE TABLE IF NOT EXISTS sales ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "gross_revenue DECIMAL(10, 2) NOT NULL, "
                + "cost_of_goods_sold DECIMAL(10, 2) DEFAULT 0.00, "
                + "operating_expenses DECIMAL(10, 2) DEFAULT 0.00, "
                + "tax_amount DECIMAL(10, 2) DEFAULT 0.00, "
                + "payment_method VARCHAR(50), "
                + "invoice_number VARCHAR(100) UNIQUE, "
                + "employee_id INT DEFAULT 0, "
                + "sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        String settingsSQL = "CREATE TABLE IF NOT EXISTS settings ("
                + "id INT PRIMARY KEY DEFAULT 1, "
                + "tax_rate DECIMAL(5,2) DEFAULT 13.00"
                + ");";

        String saleItemsSQL = "CREATE TABLE IF NOT EXISTS sale_items ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "sale_id INT NOT NULL, "
                + "product_id INT NOT NULL, "
                + "quantity INT NOT NULL, "
                + "unit_price DECIMAL(10,2) NOT NULL, "
                + "total_price DECIMAL(10,2) NOT NULL, "
                + "FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (product_id) REFERENCES products(id)"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); Statement statement = conn.createStatement()){

            statement.executeUpdate(usersSQL);
            statement.executeUpdate(productsSQL);
            statement.executeUpdate(salesSQL);
            statement.executeUpdate(settingsSQL);
            statement.executeUpdate(saleItemsSQL);
            System.out.println("Tables initialized successfully");

        }catch (Exception e){
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
    }

    public static boolean addProduct(String name, double price, int quantity, double cog, String station){
        String sql = "INSERT INTO products (name, price, quantity, cog, station, is_empty) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setDouble(4, cog);
            statement.setString(5, station);
            statement.setBoolean(6, quantity == 0);
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
                    set.getDouble("cog"),
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

    public static boolean updateProduct(int id, String name, double price, double cog, int quantity, String station){
        String sql = "UPDATE products SET name=?, price=?, cog=?, quantity=?, station=?, is_empty=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, cog);
            pstmt.setString(5, station);
            pstmt.setBoolean(6, quantity == 0);
            pstmt.setInt(7, id);

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

    // Updated addSale method
    public static boolean addSale(BigDecimal grossRevenue, BigDecimal costOfGoodsSold,
                                  BigDecimal operatingExpenses, BigDecimal taxAmount,
                                  String paymentMethod, String invoiceNumber, int employeeId) {
        String sql = "INSERT INTO sales (gross_revenue, cost_of_goods_sold, operating_expenses, " +
                "tax_amount, payment_method, invoice_number, employee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBigDecimal(1, grossRevenue);
            statement.setBigDecimal(2, costOfGoodsSold);
            statement.setBigDecimal(3, operatingExpenses);
            statement.setBigDecimal(4, taxAmount);
            statement.setString(5, paymentMethod);
            statement.setString(6, invoiceNumber);
            statement.setInt(7, employeeId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    // Simplified addSale for basic sales
    public static boolean addSale(BigDecimal grossRevenue, BigDecimal costOfGoodsSold) {
        return addSale(grossRevenue, costOfGoodsSold, BigDecimal.ZERO, BigDecimal.ZERO,
                null, null, 0);
    }

    // Updated getAllSales method
    public static List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Statement statement = conn.createStatement();
            ResultSet set = statement.executeQuery(sql)) {
            while (set.next()) {
                Sale s = new Sale(
                        set.getInt("id"),
                        set.getBigDecimal("gross_revenue"),
                        set.getBigDecimal("cost_of_goods_sold"),
                        set.getBigDecimal("operating_expenses"),
                        set.getBigDecimal("tax_amount"),
                        set.getTimestamp("sale_date")
                );
                s.setPaymentMethod(set.getString("payment_method"));
                s.setInvoiceNumber(set.getString("invoice_number"));
                s.setEmployeeId(set.getInt("employee_id"));
                sales.add(s);
            }
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
        return sales;
    }

    // Updated deleteSale method (no changes needed)
    public static boolean deleteSale(int id) {
        String sql = "DELETE FROM sales WHERE id=?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    // New: Update sale method
    public static boolean updateSale(Sale sale) {
        String sql = "UPDATE sales SET gross_revenue=?, cost_of_goods_sold=?, " +
                "operating_expenses=?, tax_amount=?, payment_method=?, " +
                "invoice_number=?, employee_id=? WHERE id=?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setBigDecimal(1, sale.getGrossRevenue());
            statement.setBigDecimal(2, sale.getCostOfGoodsSold());
            statement.setBigDecimal(3, sale.getOperatingExpenses());
            statement.setBigDecimal(4, sale.getTaxAmount());
            statement.setString(5, sale.getPaymentMethod());
            statement.setString(6, sale.getInvoiceNumber());
            statement.setInt(7, sale.getEmployeeId());
            statement.setInt(8, sale.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
            return false;
        }
    }

    public static double getTaxRate() {
        String sql = "SELECT tax_rate FROM settings WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("tax_rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 13.0;
    }

    public static boolean setTaxRate(double rate) {
        String sql = "UPDATE settings SET tax_rate = ? WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, rate);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int addSaleWithItems(List<SaleItem> items, String paymentMethod, String invoiceNumber, int employeeId) {
        BigDecimal grossRevenue = BigDecimal.ZERO;
        BigDecimal cogs = BigDecimal.ZERO;

        for (SaleItem item : items) {
            grossRevenue = grossRevenue.add(new BigDecimal(item.getTotalPrice()));
            cogs = cogs.add(new BigDecimal(item.getTotalCOG()));
        }

        double taxRate = getTaxRate();
        BigDecimal taxAmount = grossRevenue.multiply(new BigDecimal(taxRate / 100.0));
        BigDecimal operatingExpenses = BigDecimal.ZERO;

        String saleSql = "INSERT INTO sales (gross_revenue, cost_of_goods_sold, operating_expenses, " +
                "tax_amount, payment_method, invoice_number, employee_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            stmt.setBigDecimal(1, grossRevenue);
            stmt.setBigDecimal(2, cogs);
            stmt.setBigDecimal(3, operatingExpenses);
            stmt.setBigDecimal(4, taxAmount);
            stmt.setString(5, paymentMethod);
            stmt.setString(6, invoiceNumber);
            stmt.setInt(7, employeeId);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int saleId = rs.getInt(1);

                String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                    for (SaleItem item : items) {
                        itemStmt.setInt(1, saleId);
                        itemStmt.setInt(2, item.getProductId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getUnitPrice());
                        itemStmt.setDouble(5, item.getTotalPrice());
                        itemStmt.executeUpdate();

                        updateProductQuantity(conn, item.getProductId(), -item.getQuantity());
                    }
                }

                conn.commit();
                return saleId;
            }

            conn.rollback();
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void updateProductQuantity(Connection conn, int productId, int quantityChange) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    public static List<SaleItem> getSaleItems(int saleId) {
        List<SaleItem> items = new ArrayList<>();
        String sql = "SELECT si.*, p.name FROM sale_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "WHERE si.sale_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(new SaleItem(
                        rs.getInt("id"),
                        rs.getInt("sale_id"),
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("total_price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static Map<String, Integer> getTopSellingProducts(int limit) {
        Map<String, Integer> topProducts = new HashMap<>();
        String sql = "SELECT p.name, SUM(si.quantity) as total_sold " +
                "FROM sale_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sold DESC " +
                "LIMIT ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                topProducts.put(rs.getString("name"), rs.getInt("total_sold"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topProducts;
    }

    public static Map<String, Double> getProductRevenueBreakdown() {
        Map<String, Double> revenueByProduct = new HashMap<>();
        String sql = "SELECT p.name, SUM(si.total_price) as total_revenue " +
                "FROM sale_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_revenue DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                revenueByProduct.put(rs.getString("name"), rs.getDouble("total_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return revenueByProduct;
    }

     //User Operations
    public static boolean addUser(String username, String password, String position, double pay) {
        String sql = "INSERT INTO users (username, password, position, pay) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, position);
            pstmt.setDouble(4, pay);

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
                        rs.getString("password"),
                        rs.getString("position"),
                        rs.getDouble("pay")
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

    public static boolean updateUser(int id, String username, String password, String position, double pay) {
        String sql = "UPDATE users SET username=?, password=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(0, id);
            pstmt.setString(3, position);
            pstmt.setDouble(4, pay);

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
