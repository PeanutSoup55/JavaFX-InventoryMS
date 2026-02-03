package com.example.javafx_inventoryms.gui.Users;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

public class UsersPage extends VBox {
    private TableView<User> userTable;
    private ObservableList<User> userData;
    private TextField nameField, passField, posField, payField;
    private Button addBtn, refreshBtn;

    public UsersPage(){
        getStyleClass().add("vbox-container");
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
        formGrid.add(passLabel, 0, 1);
        passField = new TextField();
        passField.setPromptText("Enter Password");
        passField.setPrefWidth(300);
        formGrid.add(passField, 1, 1);

        Label posLabel = new Label("Position:");
        posLabel.getStyleClass().add("form-label");
        formGrid.add(posLabel, 0, 2);
        posField = new TextField();
        posField.setPromptText("Enter Position");
        posField.setPrefWidth(300);
        formGrid.add(posField, 1, 2);

        Label payLabel = new Label("Hourly Rate:");
        payLabel.getStyleClass().add("form-label");
        formGrid.add(payLabel, 0, 3);
        payField = new TextField();
        payField.setPromptText("$0.00");
        payField.setPrefWidth(300);
        formGrid.add(payField, 1, 3);

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
        userTable.setItems(userData);
        userTable.setPlaceholder(new Label("No users"));

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setPrefWidth(180);

        TableColumn<User, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passCol.setPrefWidth(180);

        TableColumn<User, String> posCol = new TableColumn<>("Position");
        posCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        posCol.setPrefWidth(160);

        TableColumn<User, Double> payCol = new TableColumn<>("Hourly Wage");
        payCol.setCellValueFactory(new PropertyValueFactory<>("pay"));
        payCol.setPrefWidth(100);
        payCol.setCellFactory(col -> new TableCell<User, Double>() {
            @Override
            protected void updateItem(Double pay, boolean empty) {
                super.updateItem(pay, empty);
                if (empty || pay == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", pay));
                }
            }
        });

        userTable.getColumns().addAll(idCol, nameCol, passCol, posCol, payCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox.setVgrow(userTable, Priority.ALWAYS);
        getChildren().addAll(formPanel, tableTitle, userTable);
    }
    private void loadUsers(){
        userData.clear();
        List<User> users = DatabaseOperations.getAllUsers();
        userData.addAll(users);
        clearFields();
    }
    private void addUser(){
        try {
            String name = nameField.getText().trim();
            String pass = passField.getText().trim();
            String pos = posField.getText().trim();
            String pay = payField.getText().trim();

            if (name.isEmpty()){
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Enter a name");
                return;
            }
            if (pass.length() < 8){
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Enter a password longer then 8 characters");
                return;
            }
            if (pos.isEmpty()){
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Enter a position");
                return;
            }
            double payNum = Double.parseDouble(pay);
            if (payNum < 15){
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Enter pay at or above minimum wage");
                return;
            }

            if (DatabaseOperations.addUser(name, pass, pos, payNum)){
                showAlert(Alert.AlertType.INFORMATION, "Success", "User added succesfully");
                loadUsers();
            }else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user");
            }
        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Input Error", "Enter a valid number for pay");
        }catch (Exception e){
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                System.err.println(el);
            }
        }
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
