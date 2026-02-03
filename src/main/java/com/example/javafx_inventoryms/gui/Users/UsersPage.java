package com.example.javafx_inventoryms.gui.Users;

import com.example.javafx_inventoryms.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

        VBox formPanel = new VBox(16);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(24));
        Label formTitle = new Label("Add new User");
        formPanel.getStyleClass().add("form-title");
        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("grid-pane");
        formGrid.setHgap(12);
        formGrid.setVgap(16);
        formGrid.setPadding(new Insets(16, 0, 0, 0));

        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("form-label");
        formGrid.add(nameLabel, 0, 0);
        nameField = new TextField();
        nameField.setPromptText("Enter Username");
        nameField.setPrefWidth(300);
        formGrid.add(nameField, 1, 0);

        Label passLabel = new Label("Password:");
        passLabel.getStyleClass().add("form-label");
        formGrid.add(passLabel, 0, 0);
        passField = new TextField();
        passField.setPromptText("Enter Password");
        passField.setPrefWidth(300);
        formGrid.add(passField, 1, 0);

        Label posLabel = new Label("Position:");
        posLabel.getStyleClass().add("form-label");
        formGrid.add(posLabel, 0, 0);
        posField = new TextField();
        posField.setPromptText("Enter Position");
        posField.setPrefWidth(300);
        formGrid.add(posField, 1, 0);

        Label payLabel = new Label("Hourly Rate:");
        payLabel.getStyleClass().add("form-label");
        formGrid.add(payLabel, 0, 0);
        payField = new TextField();
        payField.setPromptText("$0.00");
        payField.setPrefWidth(300);
        formGrid.add(payField, 1, 0);

        HBox buttonPanel = new HBox(12);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setPadding(new Insets(16, 0, 0, 0));

        addBtn = new Button("Add User");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> addUser());

        refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("btn-primary");
        refreshBtn.setOnAction(e -> loadUsers());

        buttonPanel.getChildren().addAll(addBtn, refreshBtn);
        formPanel.getChildren().addAll(formTitle, formGrid, buttonPanel);

        Label tableTitle = new Label("All Users");
        tableTitle.getStyleClass().add("section-title");

        userTable = new TableView<>();
        userData = FXCollections.observableArrayList();

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
