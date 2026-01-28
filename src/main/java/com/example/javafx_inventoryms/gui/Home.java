package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.gui.Products.ProductsPage;
import com.example.javafx_inventoryms.gui.Sales.SalesPage;
import com.example.javafx_inventoryms.gui.Users.UsersPage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class Home extends BorderPane {
    public Home(){
        createMenu();
    }

    public void createMenu(){
        MenuBar menuBar = new MenuBar();
        Menu salesMenu = new Menu("Sales");
        Menu productsMenu = new Menu("Products");
        Menu usersMenu = new Menu("Users");

        MenuItem productsItem = new MenuItem("View Products");
        MenuItem ordersItem = new MenuItem("View Orders");
        MenuItem usersItem = new MenuItem("View Users");

        productsMenu.getItems().add(productsItem);
        salesMenu.getItems().add(ordersItem);
        usersMenu.getItems().add(usersItem);

        menuBar.getMenus().addAll(salesMenu, productsMenu, usersMenu);

        // 🔥 ACTIONS (this is the JavaFX equivalent of ActionListener)
        productsItem.setOnAction(e -> setCenter(new ProductsPage()));
        ordersItem.setOnAction(e -> setCenter(new SalesPage()));
        usersItem.setOnAction(e-> setCenter(new UsersPage()));

        setTop(menuBar);
    }

}
