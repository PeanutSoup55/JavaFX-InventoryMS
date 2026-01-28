package com.example.javafx_inventoryms.gui;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class Home extends BorderPane {
    public Home(){
        Menu();
    }

    public void Menu(){
        MenuBar menuBar = new MenuBar();
        Menu products = new Menu("Products");
        Menu sales = new Menu("Sales");
        Menu users = new Menu("Users");

        menuBar.getMenus().addAll(products, sales, users);
        setTop(menuBar);
    }

}
