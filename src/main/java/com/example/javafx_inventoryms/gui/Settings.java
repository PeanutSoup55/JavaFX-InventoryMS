package com.example.javafx_inventoryms.gui;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Settings extends VBox {

    private TextField taxRateField;

    public Settings() {
        getStyleClass().add("vbox-container");
        setSpacing(20);
        setPadding(new Insets(20));

        initComponents();
        loadSettings();
    }

    private void initComponents() {
        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("form-title");

        VBox settingsPanel = new VBox(15);
        settingsPanel.getStyleClass().add("form-panel");
        settingsPanel.setPadding(new Insets(24));
        settingsPanel.setPrefWidth(500);

        Label sectionTitle = new Label("Tax Configuration");
        sectionTitle.getStyleClass().add("section-title");

        Label taxLabel = new Label("Tax Rate (%):");
        taxLabel.getStyleClass().add("form-label");

        taxRateField = new TextField();
        taxRateField.setPromptText("e.g., 13.00");

        Button saveButton = new Button("Save Settings");
        saveButton.getStyleClass().add("btn-primary");
        saveButton.setOnAction(e -> saveSettings());

        settingsPanel.getChildren().addAll(
                sectionTitle,
                taxLabel, taxRateField,
                saveButton
        );

        getChildren().addAll(titleLabel, settingsPanel);
    }

    private void loadSettings() {
        double taxRate = DatabaseOperations.getTaxRate();
        taxRateField.setText(String.format("%.2f", taxRate));
    }

    private void saveSettings() {
        try {
            double taxRate = Double.parseDouble(taxRateField.getText());

            if (taxRate < 0 || taxRate > 100) {
                showError("Invalid Rate", "Tax rate must be between 0 and 100.");
                return;
            }

            boolean success = DatabaseOperations.setTaxRate(taxRate);

            if (success) {
                showSuccess("Success", "Tax rate updated successfully!");
            } else {
                showError("Error", "Failed to update tax rate.");
            }

        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter a valid number.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
