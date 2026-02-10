package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.gui.Products.UpdateDeleteProducts;
import com.example.javafx_inventoryms.gui.Products.ViewAddProducts;
import com.example.javafx_inventoryms.gui.Finance.Finance;
import com.example.javafx_inventoryms.gui.Finance.Payroll;
import com.example.javafx_inventoryms.gui.Sales.SalesPage;
import com.example.javafx_inventoryms.gui.Users.UsersPage;
import com.example.javafx_inventoryms.objects.NavigationHistory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class Home extends BorderPane {
    private final NavigationHistory navigationHistory;
    private Button backButton;
    private Button forwardButton;

    public Home(){
        navigationHistory = new NavigationHistory();

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

        SalesPage initialPage = new SalesPage();
        navigationHistory.setInitialPage(initialPage);
        setCenter(initialPage);
    }

    public void createMenu(){
        MenuBar menuBar = new MenuBar();
        Menu salesMenu = new Menu("Sales");
        Menu productsMenu = new Menu("Products");
        Menu usersMenu = new Menu("Users");
        Menu financeMenu = new Menu("Finance");
        Menu settingsMenu = new Menu("Settings");
        MenuItem taxSettingsItem = new MenuItem("Tax Rate");

        MenuItem productsItem = new MenuItem("View Products");
        MenuItem productsAdd = new MenuItem("Add Product");
        MenuItem salesItem = new MenuItem("Sales Transactions");
        MenuItem financeItem = new MenuItem("Financial Overview");
        MenuItem payrollItem = new MenuItem("Payroll Management");
        MenuItem usersItem = new MenuItem("View Users");

        productsMenu.getItems().addAll(productsItem, productsAdd);
        salesMenu.getItems().addAll(salesItem);
        financeMenu.getItems().addAll(financeItem, payrollItem);
        usersMenu.getItems().addAll(usersItem);
        settingsMenu.getItems().add(taxSettingsItem);

        menuBar.getMenus().addAll(salesMenu, productsMenu, financeMenu, usersMenu, settingsMenu);

        productsAdd.setOnAction(e -> navigateToPage(new ViewAddProducts()));
        productsItem.setOnAction(e -> navigateToPage(new UpdateDeleteProducts()));
        salesItem.setOnAction(e -> navigateToPage(new SalesPage()));
        financeItem.setOnAction(e -> navigateToPage(new Finance()));
        payrollItem.setOnAction(e -> navigateToPage(new Payroll()));
        usersItem.setOnAction(e-> navigateToPage(new UsersPage()));
        taxSettingsItem.setOnAction(e -> navigateToPage(new Settings()));

        backButton = new Button("← Back");
        forwardButton = new Button("Forward →");

        backButton.setOnAction(e -> goBack());
        forwardButton.setOnAction(e -> goForward());

        backButton.getStyleClass().add("nav-button");
        forwardButton.getStyleClass().add("nav-button");

        updateNavigationButtons();

        HBox navigationBar = new HBox(10);
        navigationBar.setPadding(new Insets(5));
        navigationBar.getChildren().addAll(backButton, forwardButton);
        navigationBar.getStyleClass().add("navigation-bar");

        BorderPane topPane = new BorderPane();
        topPane.setTop(menuBar);
        topPane.setBottom(navigationBar);

        setTop(topPane);
    }

    private void navigateToPage(Node page) {
        navigationHistory.navigateTo(page);
        setCenter(page);
        updateNavigationButtons();
    }

    private void goBack() {
        Node previousPage = navigationHistory.goBack();
        if (previousPage != null) {
            setCenter(previousPage);
            updateNavigationButtons();
        }
    }

    private void goForward() {
        Node nextPage = navigationHistory.goForward();
        if (nextPage != null) {
            setCenter(nextPage);
            updateNavigationButtons();
        }
    }

    private void updateNavigationButtons() {
        backButton.setDisable(!navigationHistory.canGoBack());
        forwardButton.setDisable(!navigationHistory.canGoForward());
    }
}