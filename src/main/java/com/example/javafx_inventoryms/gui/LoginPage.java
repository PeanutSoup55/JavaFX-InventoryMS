package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.gui.Sales.SalesPage;
import com.example.javafx_inventoryms.db.DatabaseOperations;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.Objects;

public class LoginPage extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField positionField;
    private TextField payField;
    private VBox positionBox;
    private VBox payBox;
    private Button loginBtn, registerBtn;
    private boolean isRegisterMode = false;
    private Stage stage;

    public LoginPage(Stage stage) {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/javafx_inventoryms/style/login.css")).toExternalForm());

        this.stage = stage;
        stage.setTitle("Login");
        stage.setWidth(700);
        stage.setHeight(800);
        stage.centerOnScreen();

        initComponents();
    }

    private void initComponents() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setSpacing(32);
        getStyleClass().add("login-container");

        // Logo/Icon Container
        VBox logoBox = new VBox(12);
        logoBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Inventory Management");
        titleLabel.getStyleClass().add("login-title");

        Label subtitleLabel = new Label("Sign in to continue");
        subtitleLabel.getStyleClass().add("login-subtitle");

        logoBox.getChildren().addAll( titleLabel, subtitleLabel);

        // Form Container
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(360);
        formBox.getStyleClass().add("login-form");

        // Username Field
        VBox usernameBox = new VBox(8);
        Label usernameLabel = new Label("Username");
        usernameLabel.getStyleClass().add("form-label");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(44);
        usernameField.getStyleClass().add("login-field");

        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field
        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("form-label");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(44);
        passwordField.getStyleClass().add("login-field");
        passwordField.setOnAction(e -> handleLogin());

        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Position Field (for registration)
        positionBox = new VBox(8);
        Label positionLabel = new Label("Position");
        positionLabel.getStyleClass().add("form-label");

        positionField = new TextField();
        positionField.setPromptText("Enter position (e.g., Manager, Cashier)");
        positionField.setPrefHeight(44);
        positionField.getStyleClass().add("login-field");

        positionBox.getChildren().addAll(positionLabel, positionField);
        positionBox.setVisible(false);
        positionBox.setManaged(false);

        // Pay Field (for registration)
        payBox = new VBox(8);
        Label payLabel = new Label("Hourly Rate");
        payLabel.getStyleClass().add("form-label");

        payField = new TextField();
        payField.setPromptText("Enter hourly rate (e.g., 15.00)");
        payField.setPrefHeight(44);
        payField.getStyleClass().add("login-field");

        payBox.getChildren().addAll(payLabel, payField);
        payBox.setVisible(false);
        payBox.setManaged(false);

        // Button Container
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(8, 0, 0, 0));

        loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("btn-login");
        loginBtn.setPrefWidth(360);
        loginBtn.setPrefHeight(44);
        loginBtn.setOnAction(e -> {
            if (isRegisterMode) {
                handleRegister();
            } else {
                handleLogin();
            }
        });

        // Divider
        HBox dividerBox = new HBox(12);
        dividerBox.setAlignment(Pos.CENTER);
        dividerBox.setPadding(new Insets(8, 0, 8, 0));

        Separator leftLine = new Separator();
        leftLine.setPrefWidth(160);
        leftLine.getStyleClass().add("login-divider");
        HBox.setHgrow(leftLine, Priority.ALWAYS);

        Label orLabel = new Label("or");
        orLabel.getStyleClass().add("login-divider-text");

        Separator rightLine = new Separator();
        rightLine.setPrefWidth(160);
        rightLine.getStyleClass().add("login-divider");
        HBox.setHgrow(rightLine, Priority.ALWAYS);

        dividerBox.getChildren().addAll(leftLine, orLabel, rightLine);

        registerBtn = new Button("Create New Account");
        registerBtn.getStyleClass().add("btn-register");
        registerBtn.setPrefWidth(360);
        registerBtn.setPrefHeight(44);
        registerBtn.setOnAction(e -> toggleRegisterMode());

        buttonBox.getChildren().addAll(loginBtn, dividerBox, registerBtn);

        formBox.getChildren().addAll(usernameBox, passwordBox, positionBox, payBox, buttonBox);

        getChildren().addAll(logoBox, formBox);
    }

    private void toggleRegisterMode() {
        isRegisterMode = !isRegisterMode;

        if (isRegisterMode) {
            // Switch to registration mode - show additional fields
            positionBox.setVisible(true);
            positionBox.setManaged(true);
            payBox.setVisible(true);
            payBox.setManaged(true);

            loginBtn.setText("Create Account");
            registerBtn.setText("Back to Sign In");
            registerBtn.getStyleClass().clear();
            registerBtn.getStyleClass().add("btn-back");
        } else {
            // Switch back to login mode - hide additional fields
            positionBox.setVisible(false);
            positionBox.setManaged(false);
            payBox.setVisible(false);
            payBox.setManaged(false);

            loginBtn.setText("Sign In");
            registerBtn.setText("Create New Account");
            registerBtn.getStyleClass().clear();
            registerBtn.getStyleClass().add("btn-register");

            // Clear fields
            positionField.clear();
            payField.clear();
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,
                    "Login Error",
                    "Please enter both username and password!");
            return;
        }

        if (DatabaseOperations.validateUser(username, password)) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Login successful! Welcome, " + username + "!");

            // Open the main application
            javafx.application.Platform.runLater(() -> {
                Home mainApp = new Home();
                Scene scene = new Scene(mainApp, 1200, 800);

                stage.setScene(scene);
                stage.setTitle("Inventory Management System");
                stage.setMaximized(true);
            });
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Login Failed",
                    "Invalid username or password!");
            passwordField.clear();
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String position = positionField.getText().trim();
        String payStr = payField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || position.isEmpty() || payStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,
                    "Registration Error",
                    "Please fill in all fields!");
            return;
        }

        if (password.length() < 4) {
            showAlert(Alert.AlertType.ERROR,
                    "Registration Error",
                    "Password must be at least 4 characters long!");
            return;
        }

        double pay;
        try {
            pay = Double.parseDouble(payStr);
            if (pay < 0) {
                showAlert(Alert.AlertType.ERROR,
                        "Registration Error",
                        "Hourly rate must be a positive number!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Registration Error",
                    "Please enter a valid hourly rate!");
            return;
        }

        if (DatabaseOperations.addUser(username, password, position, pay)) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Registration successful! You can now login.");

            // Clear all fields and switch back to login mode
            usernameField.clear();
            passwordField.clear();
            positionField.clear();
            payField.clear();
            toggleRegisterMode();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Registration Error",
                    "Registration failed! Username might already exist.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}