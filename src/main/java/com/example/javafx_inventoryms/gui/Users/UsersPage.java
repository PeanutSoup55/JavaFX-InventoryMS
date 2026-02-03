package com.example.javafx_inventoryms.gui.Users;

import com.example.javafx_inventoryms.objects.User;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UsersPage extends VBox {
    private TableView<User> userTable;
    private ObservableList<User> userData;
    private TextField nameField, passField, posField, payField;
    private Button addBtn, refreshBtn;

    public UsersPage(){
        getStyleClass().add("vb0x-container");
        setSpacing(20);
        setPadding(new Insets(20));
        initComponents();
        loadUsers();
    }

    private void initComponents(){

    }
    private void loadUsers(){

    }
    private void addUser(){

    }
    private void clearFields(){
        nameField.clear();
        passField.clear();
        posField.clear();
        payField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
