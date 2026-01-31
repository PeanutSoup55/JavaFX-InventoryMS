package com.example.javafx_inventoryms.gui.Products;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;

public class UpdateDeleteProducts extends VBox {
    private TableView<Product> productTable;
    private ObservableList<Product> productData;
    private TextField nameField, priceField, quantityField, stationField;
    private Button updateBtn, deleteBtn, refreshBtn, clearBtn;
    private int selectedProductId = -1;

    public UpdateDeleteProducts() {
        setSpacing(10);
        setPadding(new Insets(10));

        initComponents();
        loadProducts();
    }

    private void initComponents() {
        // Table for displaying products
        Label tableTitle = new Label("Select a Product to Update or Delete");
        tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        productTable = new TableView<>();
        productData = FXCollections.observableArrayList();
        productTable.setItems(productData);

        // Define columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<Product, String> stationCol = new TableColumn<>("Station");
        stationCol.setCellValueFactory(new PropertyValueFactory<>("station"));
        stationCol.setPrefWidth(150);

        TableColumn<Product, String> emptyCol = new TableColumn<>("Empty");
        emptyCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().isEmpty() ? "Yes" : "No"
                )
        );
        emptyCol.setPrefWidth(80);

        productTable.getColumns().addAll(idCol, nameCol, priceCol, quantityCol, stationCol, emptyCol);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Table selection listener
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProductId = newSelection.getId();
                nameField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));
                quantityField.setText(String.valueOf(newSelection.getQuantity()));
                stationField.setText(newSelection.getStation());
            }
        });

        VBox.setVgrow(productTable, Priority.ALWAYS);

        // Form Panel for Updating Products
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        Label formTitle = new Label("Update/Delete Selected Product");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10, 0, 0, 0));

        // Form fields
        formGrid.add(new Label("Name:"), 0, 0);
        nameField = new TextField();
        nameField.setPromptText("Select a product from table");
        formGrid.add(nameField, 1, 0);

        formGrid.add(new Label("Price:"), 0, 1);
        priceField = new TextField();
        priceField.setPromptText("Select a product from table");
        formGrid.add(priceField, 1, 1);

        formGrid.add(new Label("Quantity:"), 0, 2);
        quantityField = new TextField();
        quantityField.setPromptText("Select a product from table");
        formGrid.add(quantityField, 1, 2);

        formGrid.add(new Label("Station:"), 0, 3);
        stationField = new TextField();
        stationField.setPromptText("Select a product from table");
        formGrid.add(stationField, 1, 3);

        // Make text fields expand
        nameField.setPrefWidth(250);
        priceField.setPrefWidth(250);
        quantityField.setPrefWidth(250);
        stationField.setPrefWidth(250);

        // Button Panel
        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setPadding(new Insets(10, 0, 0, 0));

        updateBtn = new Button("Update Product");
        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        updateBtn.setOnAction(e -> updateProduct());

        deleteBtn = new Button("Delete Product");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteProduct());

        clearBtn = new Button("Clear Selection");
        clearBtn.setOnAction(e -> clearFields());

        refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadProducts());

        buttonPanel.getChildren().addAll(updateBtn, deleteBtn, clearBtn, refreshBtn);

        formPanel.getChildren().addAll(formTitle, formGrid, buttonPanel);

        getChildren().addAll(tableTitle, productTable, formPanel);
    }

    private void loadProducts() {
        productData.clear();
        List<Product> products = DatabaseOperations.getAllProducts();
        productData.addAll(products);
        clearFields();
    }

    private void updateProduct() {
        if (selectedProductId == -1) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a product to update!");
            return;
        }

        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String station = stationField.getText().trim();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Name cannot be empty!");
                return;
            }

            if (DatabaseOperations.updateProduct(selectedProductId, name, price, quantity, station)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
                loadProducts();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product!");
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for price and quantity!");
        }
    }

    private void deleteProduct() {
        if (selectedProductId == -1) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a product to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this product?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseOperations.deleteProduct(selectedProductId)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully!");
                loadProducts();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete product!");
            }
        }
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        stationField.clear();
        selectedProductId = -1;
        productTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
