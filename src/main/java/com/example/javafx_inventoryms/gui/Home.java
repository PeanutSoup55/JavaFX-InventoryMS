package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.gui.Products.UpdateDeleteProducts;
import com.example.javafx_inventoryms.gui.Products.ViewAddProducts;
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

        MenuItem productsItem = new MenuItem("View Products");
        MenuItem productsAdd = new MenuItem("Add Product");
        MenuItem ordersItem = new MenuItem("View Orders");
        MenuItem ordersAdd = new MenuItem("Add Order");
        MenuItem usersItem = new MenuItem("View Users");
        MenuItem usersAdd = new MenuItem("Add User");

        productsMenu.getItems().addAll(productsItem, productsAdd);
        salesMenu.getItems().addAll(ordersItem, ordersAdd);
        usersMenu.getItems().addAll(usersItem, usersAdd);

        menuBar.getMenus().addAll(salesMenu, productsMenu, usersMenu);

        productsAdd.setOnAction(e -> setCenter(new ViewAddProducts()));
        productsItem.setOnAction(e -> setCenter(new UpdateDeleteProducts()));
        ordersItem.setOnAction(e -> setCenter(new SalesPage()));
        usersItem.setOnAction(e-> setCenter(new UsersPage()));

        setTop(menuBar);
    }

}
