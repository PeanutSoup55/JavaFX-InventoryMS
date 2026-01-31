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

public class ViewAddProducts extends VBox {
    private TableView<Product> productTable;
    private ObservableList<Product> productData;
    private TextField nameField, priceField, quantityField, stationField;
    private Button addBtn, refreshBtn;

    public ViewAddProducts() {
        setSpacing(10);
        setPadding(new Insets(10));

        initComponents();
        loadProducts();
    }

    private void initComponents() {
        // Form Panel for Adding Products
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        Label formTitle = new Label("Add New Product");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10, 0, 0, 0));

        // Form fields
        formGrid.add(new Label("Name:"), 0, 0);
        nameField = new TextField();
        nameField.setPromptText("Enter product name");
        formGrid.add(nameField, 1, 0);

        formGrid.add(new Label("Price:"), 0, 1);
        priceField = new TextField();
        priceField.setPromptText("Enter price");
        formGrid.add(priceField, 1, 1);

        formGrid.add(new Label("Quantity:"), 0, 2);
        quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");
        formGrid.add(quantityField, 1, 2);

        formGrid.add(new Label("Station:"), 0, 3);
        stationField = new TextField();
        stationField.setPromptText("Enter station");
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

        addBtn = new Button("Add Product");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(e -> addProduct());

        refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadProducts());

        buttonPanel.getChildren().addAll(addBtn, refreshBtn);

        formPanel.getChildren().addAll(formTitle, formGrid, buttonPanel);

        // Table for displaying products
        Label tableTitle = new Label("All Products");
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

        VBox.setVgrow(productTable, Priority.ALWAYS);

        getChildren().addAll(formPanel, tableTitle, productTable);
    }

    private void loadProducts() {
        productData.clear();
        List<Product> products = DatabaseOperations.getAllProducts();
        productData.addAll(products);
        clearFields();
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            String station = stationField.getText().trim();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Name cannot be empty!");
                return;
            }

            if (DatabaseOperations.addProduct(name, price, quantity, station)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
                loadProducts();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add product!");
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for price and quantity!");
        }
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        stationField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
