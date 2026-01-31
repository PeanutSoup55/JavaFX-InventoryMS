package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.Sale;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SalesPage extends ScrollPane {

    private TableView<Sale> salesTable;
    private TextField grossRevenueField;
    private TextField cogsField;
    private TextField operatingExpensesField;
    private TextField taxAmountField;
    private ComboBox<String> paymentMethodCombo;
    private TextField invoiceNumberField;
    private TextField employeeIdField;
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

        initComponents();
        loadSalesData();

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }

    private void initComponents() {
        // Page Title
        Label titleLabel = new Label("Sales Management");
        titleLabel.getStyleClass().add("form-title");

        // Sales Summary Cards
        HBox summaryBox = createSummaryCards();

        // Main content area
        HBox mainContent = new HBox(20);

        // Left side: Form
        VBox formPanel = createFormPanel();

        // Right side: Table and Charts
        VBox dataPanel = createDataPanel();

        HBox.setHgrow(formPanel, Priority.NEVER);
        HBox.setHgrow(dataPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(formPanel, dataPanel);

        content.getChildren().addAll(titleLabel, summaryBox, mainContent);
    }

    private HBox createSummaryCards() {
        HBox summaryBox = new HBox(15);
        summaryBox.setAlignment(Pos.CENTER);

        // Daily Sales Card
        VBox dailyCard = createSummaryCard("Today's Sales", "$0.00", "daily-sales");
        dailySalesLabel = (Label) ((VBox) dailyCard.getChildren().get(0)).getChildren().get(1);

        // Weekly Sales Card
        VBox weeklyCard = createSummaryCard("This Week", "$0.00", "weekly-sales");
        weeklySalesLabel = (Label) ((VBox) weeklyCard.getChildren().get(0)).getChildren().get(1);

        // Monthly Sales Card
        VBox monthlyCard = createSummaryCard("This Month", "$0.00", "monthly-sales");
        monthlySalesLabel = (Label) ((VBox) monthlyCard.getChildren().get(0)).getChildren().get(1);

        HBox.setHgrow(dailyCard, Priority.ALWAYS);
        HBox.setHgrow(weeklyCard, Priority.ALWAYS);
        HBox.setHgrow(monthlyCard, Priority.ALWAYS);

        summaryBox.getChildren().addAll(dailyCard, weeklyCard, monthlyCard);

        return summaryBox;
    }

    private VBox createSummaryCard(String title, String value, String id) {
        VBox card = new VBox(10);
        card.getStyleClass().add("form-panel");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setId(id);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("form-label");
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        VBox content = new VBox(5, titleLabel, valueLabel);
        content.setAlignment(Pos.CENTER);

        card.getChildren().add(content);

        return card;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(15);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPadding(new Insets(24));
        formPanel.setPrefWidth(350);

        Label formTitle = new Label("Add New Sale");
        formTitle.getStyleClass().add("section-title");

        // Gross Revenue
        Label revenueLabel = new Label("Gross Revenue ($):");
        revenueLabel.getStyleClass().add("form-label");
        grossRevenueField = new TextField();
        grossRevenueField.setPromptText("0.00");

        // COGS
        Label cogsLabel = new Label("Cost of Goods Sold ($):");
        cogsLabel.getStyleClass().add("form-label");
        cogsField = new TextField("0.00");
        cogsField.setPromptText("0.00");

        // Operating Expenses
        Label expensesLabel = new Label("Operating Expenses ($):");
        expensesLabel.getStyleClass().add("form-label");
        operatingExpensesField = new TextField("0.00");
        operatingExpensesField.setPromptText("0.00");

        // Tax Amount
        Label taxLabel = new Label("Tax Amount ($):");
        taxLabel.getStyleClass().add("form-label");
        taxAmountField = new TextField("0.00");
        taxAmountField.setPromptText("0.00");

        // Payment Method
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.getStyleClass().add("form-label");
        paymentMethodCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Cash", "Credit Card", "Debit Card", "Check", "Digital Wallet", "Bank Transfer"
        ));
        paymentMethodCombo.setValue("Cash");

        // Invoice Number
        Label invoiceLabel = new Label("Invoice Number:");
        invoiceLabel.getStyleClass().add("form-label");
        invoiceNumberField = new TextField();
        invoiceNumberField.setPromptText("INV-" + System.currentTimeMillis());

        // Employee ID
        Label employeeLabel = new Label("Employee ID:");
        employeeLabel.getStyleClass().add("form-label");
        employeeIdField = new TextField("0");
        employeeIdField.setPromptText("0");

        // Calculated Profit Display
        Label profitLabel = new Label("Calculated Profit: $0.00");
        profitLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b82f6;");

        // Auto-calculate profit when fields change
        grossRevenueField.textProperty().addListener((obs, old, newVal) -> updateCalculatedProfit(profitLabel));
        cogsField.textProperty().addListener((obs, old, newVal) -> updateCalculatedProfit(profitLabel));
        operatingExpensesField.textProperty().addListener((obs, old, newVal) -> updateCalculatedProfit(profitLabel));
        taxAmountField.textProperty().addListener((obs, old, newVal) -> updateCalculatedProfit(profitLabel));

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Add Sale");
        addButton.getStyleClass().add("btn-primary");
        addButton.setOnAction(e -> addSale());

        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("btn-secondary");
        clearButton.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addButton, clearButton);

        formPanel.getChildren().addAll(
                formTitle,
                revenueLabel, grossRevenueField,
                cogsLabel, cogsField,
                expensesLabel, operatingExpensesField,
                taxLabel, taxAmountField,
                paymentLabel, paymentMethodCombo,
                invoiceLabel, invoiceNumberField,
                employeeLabel, employeeIdField,
                profitLabel,
                buttonBox
        );

        return formPanel;
    }

    private VBox createDataPanel() {
        VBox dataPanel = new VBox(20);

        // Sales Table
        VBox tablePanel = createTablePanel();

        // Charts Row
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

        // Context menu for table
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewItem = new MenuItem("View Details");
        MenuItem deleteItem = new MenuItem("Delete");

        viewItem.setOnAction(e -> viewSaleDetails());
        deleteItem.setOnAction(e -> deleteSale());

        contextMenu.getItems().addAll(viewItem, deleteItem);
        salesTable.setContextMenu(contextMenu);

        // Action buttons
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

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue ($)");

        salesTrendChart = new LineChart<>(xAxis, yAxis);
        salesTrendChart.setTitle("");
        salesTrendChart.setLegendVisible(true);
        salesTrendChart.setPrefHeight(250);

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
        paymentMethodChart.setPrefHeight(250);

        chartPanel.getChildren().addAll(chartTitle, paymentMethodChart);

        return chartPanel;
    }

    private void updateCalculatedProfit(Label profitLabel) {
        try {
            double revenue = parseDouble(grossRevenueField.getText());
            double cogs = parseDouble(cogsField.getText());
            double expenses = parseDouble(operatingExpensesField.getText());
            double tax = parseDouble(taxAmountField.getText());

            double netRevenue = revenue - tax;
            double profit = netRevenue - cogs - expenses;

            profitLabel.setText(String.format("Calculated Profit: $%.2f", profit));

            if (profit >= 0) {
                profitLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #10b981;");
            } else {
                profitLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ef4444;");
            }
        } catch (NumberFormatException e) {
            profitLabel.setText("Calculated Profit: $0.00");
            profitLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3b82f6;");
        }
    }

    private double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) return 0.0;
        return Double.parseDouble(text.trim());
    }

    private void addSale() {
        try {
            BigDecimal grossRevenue = new BigDecimal(grossRevenueField.getText());
            BigDecimal cogs = new BigDecimal(cogsField.getText().isEmpty() ? "0" : cogsField.getText());
            BigDecimal opEx = new BigDecimal(operatingExpensesField.getText().isEmpty() ? "0" : operatingExpensesField.getText());
            BigDecimal tax = new BigDecimal(taxAmountField.getText().isEmpty() ? "0" : taxAmountField.getText());

            if (grossRevenue.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Validation Error", "Gross revenue must be greater than zero.");
                return;
            }

            String paymentMethod = paymentMethodCombo.getValue();
            String invoiceNumber = invoiceNumberField.getText().trim();
            int employeeId = Integer.parseInt(employeeIdField.getText().isEmpty() ? "0" : employeeIdField.getText());

            // Use the database operation to add sale
            boolean success = DatabaseOperations.addSale(grossRevenue, cogs, opEx, tax,
                    paymentMethod, invoiceNumber, employeeId);

            if (success) {
                showSuccess("Success", "Sale added successfully!");
                clearForm();
                loadSalesData();
            } else {
                showError("Database Error", "Failed to add sale. Please try again.");
            }

        } catch (NumberFormatException e) {
            showError("Validation Error", "Please enter valid numbers for all monetary fields.");
        }
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
        confirm.setContentText("Are you sure you want to delete this sale?");

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

    private void viewSaleDetails() {
        Sale selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selection Error", "Please select a sale to view.");
            return;
        }

        Alert details = new Alert(Alert.AlertType.INFORMATION);
        details.setTitle("Sale Details");
        details.setHeaderText("Sale #" + selected.getId());

        String content = String.format(
                "Invoice: %s\n" +
                        "Gross Revenue: $%,.2f\n" +
                        "Cost of Goods Sold: $%,.2f\n" +
                        "Operating Expenses: $%,.2f\n" +
                        "Tax Amount: $%,.2f\n" +
                        "Net Revenue: $%,.2f\n" +
                        "Profit: $%,.2f\n" +
                        "Profit Margin: %.2f%%\n" +
                        "Payment Method: %s\n" +
                        "Employee ID: %d\n" +
                        "Date: %s",
                selected.getInvoiceNumber() != null ? selected.getInvoiceNumber() : "N/A",
                selected.getGrossRevenue(),
                selected.getCostOfGoodsSold(),
                selected.getOperatingExpenses(),
                selected.getTaxAmount(),
                selected.getNetRevenue(),
                selected.getProfit(),
                selected.getProfitMargin(),
                selected.getPaymentMethod() != null ? selected.getPaymentMethod() : "N/A",
                selected.getEmployeeId(),
                selected.getSaleDate()
        );

        details.setContentText(content);
        details.showAndWait();
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
        // Update trend chart
        salesTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        Map<LocalDate, BigDecimal> dailyRevenue = new TreeMap<>();
        LocalDate today = LocalDate.now();

        // Initialize last 7 days
        for (int i = 6; i >= 0; i--) {
            dailyRevenue.put(today.minusDays(i), BigDecimal.ZERO);
        }

        // Sum revenue by date
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

        // Update payment method chart
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

    private void clearForm() {
        grossRevenueField.clear();
        cogsField.setText("0.00");
        operatingExpensesField.setText("0.00");
        taxAmountField.setText("0.00");
        paymentMethodCombo.setValue("Cash");
        invoiceNumberField.clear();
        employeeIdField.setText("0");
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