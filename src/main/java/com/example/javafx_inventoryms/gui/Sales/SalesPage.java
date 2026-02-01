package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.Product;
import com.example.javafx_inventoryms.objects.Sale;
import com.example.javafx_inventoryms.objects.SaleItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class SalesPage extends ScrollPane {

    private ComboBox<Product> productCombo;
    private Spinner<Integer> quantitySpinner;
    private TableView<SaleItem> currentSaleTable;
    private ObservableList<SaleItem> currentSaleItems;
    private TableView<Sale> salesTable;
    private ComboBox<String> paymentMethodCombo;
    private TextField invoiceNumberField;
    private Label subtotalLabel;
    private Label taxLabel;
    private Label totalLabel;
    private Label taxRateLabel;
    private Label dailySalesLabel;
    private Label weeklySalesLabel;
    private Label monthlySalesLabel;
    private LineChart<String, Number> salesTrendChart;
    private BarChart<String, Number> paymentMethodChart;
    private VBox content;

    public SalesPage() {

        content = new VBox(20);
        content.getStyleClass().add("vbox-container");
        content.setPadding(new Insets(20));

        currentSaleItems = FXCollections.observableArrayList();

        initComponents();
        loadSalesData();

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }

    private void initComponents() {
        Label titleLabel = new Label("Sales Management");
        titleLabel.getStyleClass().add("form-title");

        HBox summaryBox = createSummaryCards();
        HBox mainContent = new HBox(20);
        VBox formPanel = createFormPanel();
        VBox dataPanel = createDataPanel();

        HBox.setHgrow(formPanel, Priority.NEVER);
        HBox.setHgrow(dataPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(formPanel, dataPanel);
        content.getChildren().addAll(titleLabel, summaryBox, mainContent);
    }

    private HBox createSummaryCards() {
        HBox summaryBox = new HBox(15);
        summaryBox.setAlignment(Pos.CENTER);

        VBox dailyCard = createSummaryCard("Today's Sales", "$0.00");
        dailySalesLabel = (Label) ((VBox) dailyCard.getChildren().get(0)).getChildren().get(1);

        VBox weeklyCard = createSummaryCard("This Week", "$0.00");
        weeklySalesLabel = (Label) ((VBox) weeklyCard.getChildren().get(0)).getChildren().get(1);

        VBox monthlyCard = createSummaryCard("This Month", "$0.00");
        monthlySalesLabel = (Label) ((VBox) monthlyCard.getChildren().get(0)).getChildren().get(1);

        HBox.setHgrow(dailyCard, Priority.ALWAYS);
        HBox.setHgrow(weeklyCard, Priority.ALWAYS);
        HBox.setHgrow(monthlyCard, Priority.ALWAYS);

        summaryBox.getChildren().addAll(dailyCard, weeklyCard, monthlyCard);

        return summaryBox;
    }

    private VBox createSummaryCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("form-panel");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("form-label");
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #10b981; -fx-padding: 0 5 0 0;");

        VBox content = new VBox(5, titleLabel, valueLabel);
        content.setAlignment(Pos.CENTER);

        card.getChildren().add(content);

        return card;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(15);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(24));
        formPanel.setPrefWidth(400);

        Label formTitle = new Label("New Sale");
        formTitle.getStyleClass().add("section-title");

        double currentTaxRate = DatabaseOperations.getTaxRate();
        taxRateLabel = new Label(String.format("Tax Rate: %.1f%%", currentTaxRate));
        taxRateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        Label productLabel = new Label("Select Product:");
        productLabel.getStyleClass().add("form-label");

        productCombo = new ComboBox<>();
        productCombo.setPromptText("Choose product...");
        productCombo.setPrefWidth(Double.MAX_VALUE);
        loadProducts();

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.getStyleClass().add("form-label");

        quantitySpinner = new Spinner<>(1, 999, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(Double.MAX_VALUE);

        Button addToSaleButton = new Button("Add to Sale");
        addToSaleButton.getStyleClass().add("btn-info");
        addToSaleButton.setPrefWidth(Double.MAX_VALUE);
        addToSaleButton.setOnAction(e -> addProductToSale());

        Label currentSaleLabel = new Label("Current Sale Items:");
        currentSaleLabel.getStyleClass().add("form-label");
        currentSaleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        currentSaleTable = new TableView<>();
        currentSaleTable.setItems(currentSaleItems);
        currentSaleTable.setPrefHeight(200);
        currentSaleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SaleItem, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<SaleItem, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(50);

        TableColumn<SaleItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        priceCol.setCellFactory(col -> new TableCell<SaleItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("$%.2f", item));
            }
        });

        currentSaleTable.getColumns().addAll(nameCol, qtyCol, priceCol);

        Button removeItemButton = new Button("Remove Selected");
        removeItemButton.getStyleClass().add("btn-danger");
        removeItemButton.setOnAction(e -> removeSelectedItem());

        subtotalLabel = new Label("Subtotal: $0.00");
        subtotalLabel.setStyle("-fx-font-size: 14px;");

        taxLabel = new Label("Tax: $0.00");
        taxLabel.setStyle("-fx-font-size: 14px;");

        totalLabel = new Label("Total: $0.00");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.getStyleClass().add("form-label");

        paymentMethodCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Cash", "Credit Card", "Debit Card", "Check", "Digital Wallet", "Bank Transfer"
        ));
        paymentMethodCombo.setValue("Cash");

        Label invoiceLabel = new Label("Invoice Number:");
        invoiceLabel.getStyleClass().add("form-label");

        invoiceNumberField = new TextField();
        invoiceNumberField.setPromptText("Auto-generated");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button completeSaleButton = new Button("Enter");
        completeSaleButton.getStyleClass().add("btn-primary");
        completeSaleButton.setOnAction(e -> completeSale());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("btn-secondary");
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(completeSaleButton, clearButton);

        formPanel.getChildren().addAll(
                formTitle, taxRateLabel,
                productLabel, productCombo,
                quantityLabel, quantitySpinner,
                addToSaleButton,
                new Separator(),
                currentSaleLabel, currentSaleTable, removeItemButton,
                new Separator(),
                subtotalLabel, taxLabel, totalLabel,
                paymentLabel, paymentMethodCombo,
                invoiceLabel, invoiceNumberField,
                buttonBox
        );

        return formPanel;
    }

    private void loadProducts() {
        List<Product> products = DatabaseOperations.getAllProducts();
        productCombo.setItems(FXCollections.observableArrayList(products));

        productCombo.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - $%.2f (Stock: %d)",
                            item.getName(), item.getPrice(), item.getQuantity()));
                }
            }
        });

        productCombo.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void addProductToSale() {
        Product selected = productCombo.getValue();
        if (selected == null) {
            showError("No Product", "Please select a product.");
            return;
        }

        int quantity = quantitySpinner.getValue();
        if (quantity > selected.getQuantity()) {
            showError("Insufficient Stock",
                    String.format("Only %d units available.", selected.getQuantity()));
            return;
        }

        SaleItem item = new SaleItem(
                selected.getId(),
                selected.getName(),
                quantity,
                selected.getPrice(),
                selected.getCog()  // Add COG here
        );

        currentSaleItems.add(item);
        updateTotals();

        productCombo.setValue(null);
        quantitySpinner.getValueFactory().setValue(1);
    }

    private void removeSelectedItem() {
        SaleItem selected = currentSaleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentSaleItems.remove(selected);
            updateTotals();
        }
    }

    private void updateTotals() {
        double subtotal = currentSaleItems.stream()
                .mapToDouble(SaleItem::getTotalPrice)
                .sum();

        double taxRate = DatabaseOperations.getTaxRate();
        double tax = subtotal * (taxRate / 100.0);
        double total = subtotal + tax;

        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        taxLabel.setText(String.format("Tax (%.1f%%): $%.2f", taxRate, tax));
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    private void completeSale() {
        if (currentSaleItems.isEmpty()) {
            showError("Empty Sale", "Please add at least one product to the sale.");
            return;
        }

        String paymentMethod = paymentMethodCombo.getValue();
        String invoice = invoiceNumberField.getText().trim();
        if (invoice.isEmpty()) {
            invoice = "INV-" + System.currentTimeMillis();
        }

        int saleId = DatabaseOperations.addSaleWithItems(
                new ArrayList<>(currentSaleItems),
                paymentMethod,
                invoice,
                0
        );

        if (saleId > 0) {
            showSuccess("Success", "Sale completed successfully!");
            clearForm();
            loadSalesData();
            loadProducts();
        } else {
            showError("Error", "Failed to complete sale.");
        }
    }

    private void clearForm() {
        currentSaleItems.clear();
        productCombo.setValue(null);
        quantitySpinner.getValueFactory().setValue(1);
        paymentMethodCombo.setValue("Cash");
        invoiceNumberField.clear();
        updateTotals();
    }

    private VBox createDataPanel() {
        VBox dataPanel = new VBox(20);
        VBox tablePanel = createTablePanel();
        HBox chartsRow = new HBox(20);
        VBox trendChartPanel = createTrendChart();
        VBox paymentChartPanel = createPaymentChart();

        HBox.setHgrow(trendChartPanel, Priority.ALWAYS);
        HBox.setHgrow(paymentChartPanel, Priority.ALWAYS);

        chartsRow.getChildren().addAll(trendChartPanel, paymentChartPanel);
        VBox.setVgrow(tablePanel, Priority.ALWAYS);

        dataPanel.getChildren().addAll(tablePanel, chartsRow);

        return dataPanel;
    }

    private VBox createTablePanel() {
        VBox tablePanel = new VBox(10);
        tablePanel.getStyleClass().add("form-panel");
        tablePanel.setPadding(new Insets(20));

        Label tableTitle = new Label("Recent Sales");
        tableTitle.getStyleClass().add("section-title");

        salesTable = new TableView<>();
        salesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Sale, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Sale, String> invoiceCol = new TableColumn<>("Invoice");
        invoiceCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));

        TableColumn<Sale, BigDecimal> revenueCol = new TableColumn<>("Revenue");
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("netRevenue"));
        revenueCol.setCellFactory(col -> new TableCell<Sale, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("$%,.2f", item));
            }
        });

        TableColumn<Sale, BigDecimal> profitCol = new TableColumn<>("Profit");
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profit"));
        profitCol.setCellFactory(col -> new TableCell<Sale, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("$%,.2f", item));
                    if (item.compareTo(BigDecimal.ZERO) >= 0) {
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<Sale, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Sale, Timestamp> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));

        salesTable.getColumns().addAll(idCol, invoiceCol, revenueCol, profitCol, paymentCol, dateCol);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View Items");
        MenuItem deleteItem = new MenuItem("Delete");

        viewItem.setOnAction(e -> viewSaleItems());
        deleteItem.setOnAction(e -> deleteSale());

        contextMenu.getItems().addAll(viewItem, deleteItem);
        salesTable.setContextMenu(contextMenu);

        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Button deleteButton = new Button("Delete Selected");
        deleteButton.getStyleClass().add("btn-danger");
        deleteButton.setOnAction(e -> deleteSale());

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("btn-info");
        refreshButton.setOnAction(e -> loadSalesData());

        actionBox.getChildren().addAll(refreshButton, deleteButton);

        tablePanel.getChildren().addAll(tableTitle, salesTable, actionBox);
        VBox.setVgrow(salesTable, Priority.ALWAYS);

        return tablePanel;
    }

    private VBox createTrendChart() {
        VBox chartPanel = new VBox(10);
        chartPanel.getStyleClass().add("form-panel");
        chartPanel.setPadding(new Insets(20));

        Label chartTitle = new Label("Sales Trend (Last 7 Days)");
        chartTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setTickLabelRotation(45);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue ($)");

        salesTrendChart = new LineChart<>(xAxis, yAxis);
        salesTrendChart.setTitle("");
        salesTrendChart.setLegendVisible(true);
        salesTrendChart.setPrefHeight(350);

        chartPanel.getChildren().addAll(chartTitle, salesTrendChart);

        return chartPanel;
    }

    private VBox createPaymentChart() {
        VBox chartPanel = new VBox(10);
        chartPanel.getStyleClass().add("form-panel");
        chartPanel.setPadding(new Insets(20));

        Label chartTitle = new Label("Sales by Payment Method");
        chartTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Payment Method");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total ($)");

        paymentMethodChart = new BarChart<>(xAxis, yAxis);
        paymentMethodChart.setTitle("");
        paymentMethodChart.setLegendVisible(false);
        paymentMethodChart.setPrefHeight(350);

        chartPanel.getChildren().addAll(chartTitle, paymentMethodChart);

        return chartPanel;
    }

    private void viewSaleItems() {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selection Error", "Please select a sale.");
            return;
        }

        List<SaleItem> items = DatabaseOperations.getSaleItems(selected.getId());

        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Sale Items");
        dialog.setHeaderText("Sale #" + selected.getId() + " - Invoice: " + selected.getInvoiceNumber());

        StringBuilder content = new StringBuilder();
        for (SaleItem item : items) {
            content.append(String.format("%s x%d @ $%.2f = $%.2f\n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalPrice()));
        }

        dialog.setContentText(content.toString());
        dialog.showAndWait();
    }

    private void deleteSale() {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selection Error", "Please select a sale to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Sale");
        confirm.setContentText("WARNING: This will NOT restore product quantities. Continue?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = DatabaseOperations.deleteSale(selected.getId());
            if (success) {
                showSuccess("Success", "Sale deleted successfully!");
                loadSalesData();
            } else {
                showError("Database Error", "Failed to delete sale.");
            }
        }
    }

    private void loadSalesData() {
        List<Sale> sales = DatabaseOperations.getAllSales();
        salesTable.getItems().clear();
        salesTable.getItems().addAll(sales);

        updateSummaryCards(sales);
        updateCharts(sales);
    }

    private void updateSummaryCards(List<Sale> sales) {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.withDayOfMonth(1);

        BigDecimal dailyTotal = BigDecimal.ZERO;
        BigDecimal weeklyTotal = BigDecimal.ZERO;
        BigDecimal monthlyTotal = BigDecimal.ZERO;

        for (Sale sale : sales) {
            LocalDate saleDate = sale.getSaleDate().toLocalDateTime().toLocalDate();
            BigDecimal revenue = sale.getNetRevenue();

            if (saleDate.equals(today)) {
                dailyTotal = dailyTotal.add(revenue);
            }
            if (!saleDate.isBefore(weekStart)) {
                weeklyTotal = weeklyTotal.add(revenue);
            }
            if (!saleDate.isBefore(monthStart)) {
                monthlyTotal = monthlyTotal.add(revenue);
            }
        }

        dailySalesLabel.setText(String.format("$%,.2f", dailyTotal));
        weeklySalesLabel.setText(String.format("$%,.2f", weeklyTotal));
        monthlySalesLabel.setText(String.format("$%,.2f", monthlyTotal));
    }

    private void updateCharts(List<Sale> sales) {
        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        Map<LocalDate, BigDecimal> dailyRevenue = new TreeMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            dailyRevenue.put(today.minusDays(i), BigDecimal.ZERO);
        }

        for (Sale sale : sales) {
            LocalDate date = sale.getSaleDate().toLocalDateTime().toLocalDate();
            if (dailyRevenue.containsKey(date)) {
                dailyRevenue.put(date, dailyRevenue.get(date).add(sale.getNetRevenue()));
            }
        }

        for (Map.Entry<LocalDate, BigDecimal> entry : dailyRevenue.entrySet()) {
            series.getData().add(new XYChart.Data<>(
                    entry.getKey().toString(),
                    entry.getValue().doubleValue()
            ));
        }

        salesTrendChart.getData().add(series);

        paymentMethodChart.getData().clear();
        XYChart.Series<String, Number> paymentSeries = new XYChart.Series<>();
        paymentSeries.setName("Sales");

        Map<String, BigDecimal> paymentTotals = new HashMap<>();
        for (Sale sale : sales) {
            String method = sale.getPaymentMethod() != null ? sale.getPaymentMethod() : "Unknown";
            paymentTotals.merge(method, sale.getNetRevenue(), BigDecimal::add);
        }

        for (Map.Entry<String, BigDecimal> entry : paymentTotals.entrySet()) {
            paymentSeries.getData().add(new XYChart.Data<>(
                    entry.getKey(),
                    entry.getValue().doubleValue()
            ));
        }

        paymentMethodChart.getData().add(paymentSeries);
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