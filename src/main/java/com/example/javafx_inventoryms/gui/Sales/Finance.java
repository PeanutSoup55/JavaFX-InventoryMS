package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.Sale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Finance extends ScrollPane {

    private Label totalRevenueLabel;
    private Label totalProfitLabel;
    private Label profitMarginLabel;
    private Label totalCOGSLabel;
    private Label totalExpensesLabel;
    private LineChart<String, Number> revenueChart;
    private PieChart expenseBreakdownChart;
    private BarChart<String, Number> profitComparisonChart;
    private VBox content;

    public Finance() {
        content = new VBox(20);
        content.getStyleClass().add("vbox-container");
        content.setPadding(new Insets(20));

        initComponents();
        loadFinancialData();

        setContent(content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    }

    private void initComponents() {
        // Page Title
        Label titleLabel = new Label("Financial Overview");
        titleLabel.getStyleClass().add("form-title");

        // Key Metrics Cards
        HBox metricsBox = createMetricsCards();

        // Charts Section
        HBox chartsRow1 = new HBox(20);
        chartsRow1.setAlignment(Pos.CENTER);

        // Revenue Trend Chart
        VBox revenueChartPanel = createRevenueChart();

        // Expense Breakdown Pie Chart
        VBox expenseChartPanel = createExpenseChart();

        chartsRow1.getChildren().addAll(revenueChartPanel, expenseChartPanel);
        HBox.setHgrow(revenueChartPanel, Priority.ALWAYS);
        HBox.setHgrow(expenseChartPanel, Priority.ALWAYS);

        // Profit Comparison Bar Chart
        VBox profitChartPanel = createProfitChart();

        // Product Analytics
        VBox productAnalyticsPanel = createProductAnalyticsPanel();

        // Refresh Button
        Button refreshButton = new Button("Refresh Data");
        refreshButton.getStyleClass().add("btn-primary");
        refreshButton.setOnAction(e -> loadFinancialData());

        HBox buttonBox = new HBox(refreshButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(
                titleLabel,
                metricsBox,
                chartsRow1,
                profitChartPanel,
                productAnalyticsPanel,
                buttonBox
        );
    }

    private HBox createMetricsCards() {
        HBox metricsBox = new HBox(15);
        metricsBox.setAlignment(Pos.CENTER);

        // Total Revenue Card
        VBox revenueCard = createMetricCard("Total Revenue", "$0.00", "total-revenue");
        totalRevenueLabel = (Label) ((VBox) revenueCard.getChildren().get(0)).getChildren().get(1);

        // Total Profit Card
        VBox profitCard = createMetricCard("Total Profit", "$0.00", "total-profit");
        totalProfitLabel = (Label) ((VBox) profitCard.getChildren().get(0)).getChildren().get(1);

        // Profit Margin Card
        VBox marginCard = createMetricCard("Profit Margin", "0.00%", "profit-margin");
        profitMarginLabel = (Label) ((VBox) marginCard.getChildren().get(0)).getChildren().get(1);

        // Total COGS Card
        VBox cogsCard = createMetricCard("Total COGS", "$0.00", "total-cogs");
        totalCOGSLabel = (Label) ((VBox) cogsCard.getChildren().get(0)).getChildren().get(1);

        // Total Expenses Card
        VBox expensesCard = createMetricCard("Operating Expenses", "$0.00", "total-expenses");
        totalExpensesLabel = (Label) ((VBox) expensesCard.getChildren().get(0)).getChildren().get(1);

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(profitCard, Priority.ALWAYS);
        HBox.setHgrow(marginCard, Priority.ALWAYS);
        HBox.setHgrow(cogsCard, Priority.ALWAYS);
        HBox.setHgrow(expensesCard, Priority.ALWAYS);

        metricsBox.getChildren().addAll(revenueCard, profitCard, marginCard, cogsCard, expensesCard);

        return metricsBox;
    }

    private VBox createMetricCard(String title, String value, String id) {
        VBox card = new VBox(10);
        card.getStyleClass().add("form-panel");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setId(id);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("form-label");
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 500; -fx-text-fill: #1e293b;");

        VBox content = new VBox(5, titleLabel, valueLabel);
        content.setAlignment(Pos.CENTER);

        card.getChildren().add(content);

        return card;
    }

    private VBox createRevenueChart() {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("form-panel");
        panel.setPadding(new Insets(20));

        Label chartTitle = new Label("Revenue Trend (Last 30 Days)");
        chartTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        xAxis.setTickLabelRotation(45);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Revenue ($)");

        revenueChart = new LineChart<>(xAxis, yAxis);
        revenueChart.setTitle("");
        revenueChart.setLegendVisible(true);
        revenueChart.setPrefHeight(600);

        panel.getChildren().addAll(chartTitle, revenueChart);
        VBox.setVgrow(revenueChart, Priority.ALWAYS);

        return panel;
    }

    private VBox createExpenseChart() {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("form-panel");
        panel.setPadding(new Insets(20));

        Label chartTitle = new Label("Expense Breakdown");
        chartTitle.getStyleClass().add("section-title");

        expenseBreakdownChart = new PieChart();
        expenseBreakdownChart.setTitle("");
        expenseBreakdownChart.setLegendVisible(true);
        expenseBreakdownChart.setPrefHeight(600);

        panel.getChildren().addAll(chartTitle, expenseBreakdownChart);
        VBox.setVgrow(expenseBreakdownChart, Priority.ALWAYS);

        return panel;
    }

    private VBox createProfitChart() {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("form-panel");
        panel.setPadding(new Insets(20));

        Label chartTitle = new Label("Revenue vs Profit Comparison");
        chartTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Period");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");

        profitComparisonChart = new BarChart<>(xAxis, yAxis);
        profitComparisonChart.setTitle("");
        profitComparisonChart.setLegendVisible(true);
        profitComparisonChart.setPrefHeight(600);

        panel.getChildren().addAll(chartTitle, profitComparisonChart);
        VBox.setVgrow(profitComparisonChart, Priority.ALWAYS);

        return panel;
    }

    private void loadFinancialData() {
        List<Sale> sales = DatabaseOperations.getAllSales();

        if (sales.isEmpty()) {
            showNoDataMessage();
            return;
        }

        // Calculate totals
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;
        BigDecimal totalCOGS = BigDecimal.ZERO;
        BigDecimal totalOpEx = BigDecimal.ZERO;

        for (Sale sale : sales) {
            totalRevenue = totalRevenue.add(sale.getNetRevenue());
            totalProfit = totalProfit.add(sale.getProfit());
            totalCOGS = totalCOGS.add(sale.getCostOfGoodsSold());
            totalOpEx = totalOpEx.add(sale.getOperatingExpenses());
        }

        // Update metric cards
        totalRevenueLabel.setText(String.format("$%,.2f", totalRevenue));
        totalProfitLabel.setText(String.format("$%,.2f", totalProfit));
        totalCOGSLabel.setText(String.format("$%,.2f", totalCOGS));
        totalExpensesLabel.setText(String.format("$%,.2f", totalOpEx));

        // Calculate profit margin
        BigDecimal profitMargin = BigDecimal.ZERO;
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            profitMargin = totalProfit.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        profitMarginLabel.setText(String.format("%.2f%%", profitMargin));

        // Set label colors based on profit
        if (totalProfit.compareTo(BigDecimal.ZERO) >= 0) {
            totalProfitLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 500; -fx-text-fill: #10b981;");
        } else {
            totalProfitLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 500; -fx-text-fill: #ef4444;");
        }

        // Load charts
        loadRevenueChart(sales);
        loadExpenseChart(totalCOGS, totalOpEx);
        loadProfitComparisonChart(sales);
    }

    private void loadRevenueChart(List<Sale> sales) {
        revenueChart.getData().clear();

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Net Revenue");

        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");

        // Group by date and sum
        Map<LocalDate, BigDecimal> revenueByDate = sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().toLocalDateTime().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, Sale::getNetRevenue, BigDecimal::add)
                ));

        Map<LocalDate, BigDecimal> profitByDate = sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().toLocalDateTime().toLocalDate(),
                        Collectors.reducing(BigDecimal.ZERO, Sale::getProfit, BigDecimal::add)
                ));

        // Add data points (last 30 days)
        LocalDate today = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.toString();

            BigDecimal revenue = revenueByDate.getOrDefault(date, BigDecimal.ZERO);
            BigDecimal profit = profitByDate.getOrDefault(date, BigDecimal.ZERO);

            revenueSeries.getData().add(new XYChart.Data<>(dateStr, revenue.doubleValue()));
            profitSeries.getData().add(new XYChart.Data<>(dateStr, profit.doubleValue()));
        }

        revenueChart.getData().addAll(revenueSeries, profitSeries);
    }

    private void loadExpenseChart(BigDecimal totalCOGS, BigDecimal totalOpEx) {
        expenseBreakdownChart.getData().clear();

        PieChart.Data cogsData = new PieChart.Data(
                String.format("COGS ($%,.2f)", totalCOGS),
                totalCOGS.doubleValue()
        );

        PieChart.Data opexData = new PieChart.Data(
                String.format("Operating Expenses ($%,.2f)", totalOpEx),
                totalOpEx.doubleValue()
        );

        expenseBreakdownChart.getData().addAll(cogsData, opexData);
    }

    private void loadProfitComparisonChart(List<Sale> sales) {
        profitComparisonChart.getData().clear();

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");

        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Total Expenses");

        // Calculate weekly totals
        Map<String, BigDecimal> weeklyRevenue = calculateWeeklyTotals(sales, Sale::getNetRevenue);
        Map<String, BigDecimal> weeklyProfit = calculateWeeklyTotals(sales, Sale::getProfit);
        Map<String, BigDecimal> weeklyExpenses = calculateWeeklyExpenses(sales);

        for (String week : weeklyRevenue.keySet()) {
            revenueSeries.getData().add(new XYChart.Data<>(week, weeklyRevenue.get(week).doubleValue()));
            profitSeries.getData().add(new XYChart.Data<>(week, weeklyProfit.get(week).doubleValue()));
            expenseSeries.getData().add(new XYChart.Data<>(week, weeklyExpenses.get(week).doubleValue()));
        }

        profitComparisonChart.getData().addAll(revenueSeries, expenseSeries, profitSeries);
    }

    private Map<String, BigDecimal> calculateWeeklyTotals(
            List<Sale> sales,
            java.util.function.Function<Sale, BigDecimal> valueExtractor
    ) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> "Week " + getWeekOfYear(sale.getSaleDate()),
                        Collectors.reducing(BigDecimal.ZERO, valueExtractor, BigDecimal::add)
                ));
    }

    private Map<String, BigDecimal> calculateWeeklyExpenses(List<Sale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> "Week " + getWeekOfYear(sale.getSaleDate()),
                        Collectors.reducing(BigDecimal.ZERO,
                                sale -> sale.getCostOfGoodsSold().add(sale.getOperatingExpenses()),
                                BigDecimal::add)
                ));
    }

    private int getWeekOfYear(Timestamp timestamp) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.getDayOfYear() / 7 + 1;
    }

    private void showNoDataMessage() {
        totalRevenueLabel.setText("$0.00");
        totalProfitLabel.setText("$0.00");
        profitMarginLabel.setText("0.00%");
        totalCOGSLabel.setText("$0.00");
        totalExpensesLabel.setText("$0.00");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Data");
        alert.setHeaderText(null);
        alert.setContentText("No sales data available. Add sales to see financial metrics.");
        alert.showAndWait();
    }
    private VBox createProductAnalyticsPanel() {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("form-panel");
        panel.setPadding(new Insets(20));

        Label chartTitle = new Label("Top Selling Products");
        chartTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Product");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units Sold");

        BarChart<String, Number> topProductsChart = new BarChart<>(xAxis, yAxis);
        topProductsChart.setTitle("");
        topProductsChart.setLegendVisible(false);
        topProductsChart.setPrefHeight(400);

        Map<String, Integer> topProducts = DatabaseOperations.getTopSellingProducts(10);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Units Sold");

        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        topProductsChart.getData().add(series);

        panel.getChildren().addAll(chartTitle, topProductsChart);

        return panel;
    }
}