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
        getStyleClass().add("vbox-container");
        setSpacing(20);
        setPadding(new Insets(20));

        initComponents();
        loadProducts();
    }

    private void initComponents() {
        // Form Panel for Adding Products
        VBox formPanel = new VBox(16);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(24));

        Label formTitle = new Label("Add New Product");
        formTitle.getStyleClass().add("form-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("grid-pane");
        formGrid.setHgap(12);
        formGrid.setVgap(16);
        formGrid.setPadding(new Insets(16, 0, 0, 0));

        // Form fields with modern labels
        Label nameLabel = new Label("Name:");
        nameLabel.getStyleClass().add("form-label");
        formGrid.add(nameLabel, 0, 0);
        nameField = new TextField();
        nameField.setPromptText("Enter product name");
        nameField.setPrefWidth(300);
        formGrid.add(nameField, 1, 0);

        Label priceLabel = new Label("Price:");
        priceLabel.getStyleClass().add("form-label");
        formGrid.add(priceLabel, 0, 1);
        priceField = new TextField();
        priceField.setPromptText("0.00");
        priceField.setPrefWidth(300);
        formGrid.add(priceField, 1, 1);

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("form-label");
        formGrid.add(quantityLabel, 0, 2);
        quantityField = new TextField();
        quantityField.setPromptText("0");
        quantityField.setPrefWidth(300);
        formGrid.add(quantityField, 1, 2);

        Label stationLabel = new Label("Station:");
        stationLabel.getStyleClass().add("form-label");
        formGrid.add(stationLabel, 0, 3);
        stationField = new TextField();
        stationField.setPromptText("Enter station name");
        stationField.setPrefWidth(300);
        formGrid.add(stationField, 1, 3);

        // Button Panel
        HBox buttonPanel = new HBox(12);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setPadding(new Insets(16, 0, 0, 0));

        addBtn = new Button("Add Product");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> addProduct());

        refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("btn-secondary");
        refreshBtn.setOnAction(e -> loadProducts());

        buttonPanel.getChildren().addAll(addBtn, refreshBtn);

        formPanel.getChildren().addAll(formTitle, formGrid, buttonPanel);

        // Table for displaying products
        Label tableTitle = new Label("All Products");
        tableTitle.getStyleClass().add("section-title");

        productTable = new TableView<>();
        productData = FXCollections.observableArrayList();
        productTable.setItems(productData);
        productTable.setPlaceholder(new Label("No products available. Add one to get started!"));

        // Define columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        TableColumn<Product, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<Product, String> stationCol = new TableColumn<>("Station");
        stationCol.setCellValueFactory(new PropertyValueFactory<>("station"));
        stationCol.setPrefWidth(150);

        TableColumn<Product, String> emptyCol = new TableColumn<>("Status");
        emptyCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().isEmpty() ? "Empty" : "In Stock"
                )
        );
        emptyCol.setPrefWidth(100);
        emptyCol.setCellFactory(col -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if (status.equals("Empty")) {
                        setStyle("-fx-text-fill: #ef4444; -fx-font-weight: 600;");
                    } else {
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: 600;");
                    }
                }
            }
        });

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
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String station = stationField.getText().trim();

            // Validation
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Name cannot be empty!");
                return;
            }

            if (priceText.isEmpty() || quantityText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Price and quantity cannot be empty!");
                return;
            }

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price < 0 || quantity < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Price and quantity must be positive!");
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
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + ex.getMessage());
            ex.printStackTrace();
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