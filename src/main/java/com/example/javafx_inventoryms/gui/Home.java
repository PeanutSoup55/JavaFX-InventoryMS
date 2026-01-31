package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.gui.Products.UpdateDeleteProducts;
import com.example.javafx_inventoryms.gui.Products.ViewAddProducts;
import com.example.javafx_inventoryms.gui.Sales.Finance;
import com.example.javafx_inventoryms.gui.Sales.Payroll;
import com.example.javafx_inventoryms.gui.Sales.SalesPage;
import com.example.javafx_inventoryms.gui.Users.UsersPage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class Home extends BorderPane {
    public Home(){
        createMenu();
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/javafx_inventoryms/style/menu.css")).toExternalForm());
        try {
            getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/javafx_inventoryms/style/products.css")).toExternalForm());
        } catch (Exception e) {
            System.out.println("Failed to load CSS: " + e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
    }

    public void createMenu(){
        MenuBar menuBar = new MenuBar();
        Menu salesMenu = new Menu("Sales");
        Menu productsMenu = new Menu("Products");
        Menu usersMenu = new Menu("Users");
        Menu financeMenu = new Menu("Finance");

        MenuItem productsItem = new MenuItem("View Products");
        MenuItem productsAdd = new MenuItem("Add Product");
        MenuItem salesItem = new MenuItem("Sales Transactions");
        MenuItem financeItem = new MenuItem("Financial Overview");
        MenuItem payrollItem = new MenuItem("Payroll Management");
        MenuItem usersItem = new MenuItem("View Users");
        MenuItem usersAdd = new MenuItem("Add User");

        productsMenu.getItems().addAll(productsItem, productsAdd);
        salesMenu.getItems().addAll(salesItem);
        financeMenu.getItems().addAll(financeItem, payrollItem);
        usersMenu.getItems().addAll(usersItem, usersAdd);

        menuBar.getMenus().addAll(salesMenu, productsMenu, financeMenu, usersMenu);

        productsAdd.setOnAction(e -> setCenter(new ViewAddProducts()));
        productsItem.setOnAction(e -> setCenter(new UpdateDeleteProducts()));
        salesItem.setOnAction(e -> setCenter(new SalesPage()));
        financeItem.setOnAction(e -> setCenter(new Finance()));
        payrollItem.setOnAction(e -> setCenter(new Payroll()));
        usersItem.setOnAction(e-> setCenter(new UsersPage()));

        setTop(menuBar);
    }

}
