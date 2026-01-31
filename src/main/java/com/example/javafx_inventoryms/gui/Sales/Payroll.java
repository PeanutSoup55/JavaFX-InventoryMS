package com.example.javafx_inventoryms.gui.Sales;

import com.example.javafx_inventoryms.db.DatabaseOperations;
import com.example.javafx_inventoryms.objects.Sale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Payroll extends VBox {

    private TextField payrollPercentageField;
    private TextField fixedPayrollField;
    private RadioButton percentageRadio;
    private RadioButton fixedRadio;
    private Label totalRevenueLabel;
    private Label calculatedPayrollLabel;
    private Label profitBeforePayrollLabel;
    private Label profitAfterPayrollLabel;
    private Label payrollRateLabel;
    private PieChart breakdownChart;

    public Payroll() {
        getStyleClass().add("vbox-container");
        setSpacing(20);
        setPadding(new Insets(20));

        initComponents();
        calculatePayroll();
    }

    private void initComponents() {
        // Page Title
        Label titleLabel = new Label("Payroll Calculator");
        titleLabel.getStyleClass().add("form-title");

        // Summary Cards Row
        HBox summaryBox = createSummaryCards();

        // Main Content
        HBox mainContent = new HBox(20);

        // Left: Controls
        VBox controlPanel = createControlPanel();

        // Right: Visualization
        VBox chartPanel = createChartPanel();

        HBox.setHgrow(controlPanel, Priority.NEVER);
        HBox.setHgrow(chartPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(controlPanel, chartPanel);

        getChildren().addAll(titleLabel, summaryBox, mainContent);
    }

    private HBox createSummaryCards() {
        HBox summaryBox = new HBox(15);
        summaryBox.setAlignment(Pos.CENTER);

        // Total Revenue
        VBox revenueCard = createMetricCard("Total Revenue", "$0.00");
        totalRevenueLabel = (Label) ((VBox) revenueCard.getChildren().get(0)).getChildren().get(1);

        // Payroll Cost
        VBox payrollCard = createMetricCard("Payroll Cost", "$0.00");
        calculatedPayrollLabel = (Label) ((VBox) payrollCard.getChildren().get(0)).getChildren().get(1);
        calculatedPayrollLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ef4444;");

        // Payroll Rate
        VBox rateCard = createMetricCard("Payroll Rate", "0.00%");
        payrollRateLabel = (Label) ((VBox) rateCard.getChildren().get(0)).getChildren().get(1);
        payrollRateLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #6366f1;");

        // Profit Before Payroll
        VBox beforeCard = createMetricCard("Profit Before Payroll", "$0.00");
        profitBeforePayrollLabel = (Label) ((VBox) beforeCard.getChildren().get(0)).getChildren().get(1);

        // Profit After Payroll
        VBox afterCard = createMetricCard("Profit After Payroll", "$0.00");
        profitAfterPayrollLabel = (Label) ((VBox) afterCard.getChildren().get(0)).getChildren().get(1);

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(payrollCard, Priority.ALWAYS);
        HBox.setHgrow(rateCard, Priority.ALWAYS);
        HBox.setHgrow(beforeCard, Priority.ALWAYS);
        HBox.setHgrow(afterCard, Priority.ALWAYS);

        summaryBox.getChildren().addAll(revenueCard, payrollCard, rateCard, beforeCard, afterCard);

        return summaryBox;
    }

    private VBox createMetricCard(String title, String value) {
        VBox card = new VBox(10);
        card.getStyleClass().add("form-panel");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("form-label");
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        VBox content = new VBox(5, titleLabel, valueLabel);
        content.setAlignment(Pos.CENTER);

        card.getChildren().add(content);

        return card;
    }

    private VBox createControlPanel() {
        VBox controlPanel = new VBox(20);
        controlPanel.getStyleClass().add("form-panel");
        controlPanel.setPadding(new Insets(24));
        controlPanel.setPrefWidth(350);

        Label controlTitle = new Label("Payroll Settings");
        controlTitle.getStyleClass().add("section-title");

        // Calculation Mode
        Label modeLabel = new Label("Calculation Method:");
        modeLabel.getStyleClass().add("form-label");

        ToggleGroup modeGroup = new ToggleGroup();

        percentageRadio = new RadioButton("Percentage of Revenue");
        percentageRadio.setToggleGroup(modeGroup);
        percentageRadio.setSelected(true);

        fixedRadio = new RadioButton("Fixed Amount");
        fixedRadio.setToggleGroup(modeGroup);

        VBox radioBox = new VBox(8, percentageRadio, fixedRadio);

        // Percentage Input
        Label percentLabel = new Label("Payroll Percentage (%):");
        percentLabel.getStyleClass().add("form-label");

        payrollPercentageField = new TextField("20.00");
        payrollPercentageField.setPromptText("e.g., 20 for 20%");

        // Fixed Amount Input
        Label fixedLabel = new Label("Fixed Payroll Amount ($):");
        fixedLabel.getStyleClass().add("form-label");

        fixedPayrollField = new TextField("0.00");
        fixedPayrollField.setPromptText("e.g., 5000.00");
        fixedPayrollField.setDisable(true);

        // Toggle enable/disable
        percentageRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            payrollPercentageField.setDisable(!newVal);
            fixedPayrollField.setDisable(newVal);
            calculatePayroll();
        });

        // Real-time updates
        payrollPercentageField.textProperty().addListener((obs, old, newVal) -> calculatePayroll());
        fixedPayrollField.textProperty().addListener((obs, old, newVal) -> calculatePayroll());

        // Refresh Button
        Button refreshButton = new Button("Recalculate");
        refreshButton.getStyleClass().add("btn-primary");
        refreshButton.setOnAction(e -> calculatePayroll());
        refreshButton.setPrefWidth(Double.MAX_VALUE);

        // Info Box
        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: #f0f9ff; -fx-padding: 16; -fx-background-radius: 8;");

        Label infoTitle = new Label("ℹ️ How This Works");
        infoTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #0369a1; -fx-font-size: 14px;");

        Label infoText = new Label(
                "This calculator shows how payroll costs affect your profit.\n\n" +
                        "• Percentage Mode: Payroll is calculated as a % of total revenue\n" +
                        "• Fixed Mode: Enter a specific dollar amount\n\n" +
                        "Adjust the values to see how payroll impacts your bottom line in real-time."
        );
        infoText.setWrapText(true);
        infoText.setStyle("-fx-font-size: 12px; -fx-text-fill: #0369a1;");

        infoBox.getChildren().addAll(infoTitle, infoText);

        controlPanel.getChildren().addAll(
                controlTitle,
                modeLabel,
                radioBox,
                new Separator(),
                percentLabel,
                payrollPercentageField,
                fixedLabel,
                fixedPayrollField,
                refreshButton,
                new Separator(),
                infoBox
        );

        return controlPanel;
    }

    private VBox createChartPanel() {
        VBox chartPanel = new VBox(15);
        chartPanel.getStyleClass().add("form-panel");
        chartPanel.setPadding(new Insets(24));

        Label chartTitle = new Label("Financial Breakdown");
        chartTitle.getStyleClass().add("section-title");

        breakdownChart = new PieChart();
        breakdownChart.setTitle("");
        breakdownChart.setLegendVisible(true);
        breakdownChart.setLegendSide(javafx.geometry.Side.RIGHT);
        breakdownChart.setPrefHeight(400);

        // Explanation
        VBox explanationBox = new VBox(10);
        explanationBox.setStyle("-fx-background-color: #fefce8; -fx-padding: 16; -fx-background-radius: 8;");

        Label explanationTitle = new Label("💡 What You're Seeing");
        explanationTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #854d0e; -fx-font-size: 14px;");

        Label explanationText = new Label(
                "The pie chart shows where your revenue goes:\n\n" +
                        "• COGS: Cost of products sold\n" +
                        "• Operating Expenses: Business overhead\n" +
                        "• Payroll: Employee costs (adjustable above)\n" +
                        "• Net Profit: What's left after all expenses"
        );
        explanationText.setWrapText(true);
        explanationText.setStyle("-fx-font-size: 12px; -fx-text-fill: #854d0e;");

        explanationBox.getChildren().addAll(explanationTitle, explanationText);

        chartPanel.getChildren().addAll(chartTitle, breakdownChart, explanationBox);
        VBox.setVgrow(breakdownChart, Priority.ALWAYS);

        return chartPanel;
    }

    private void calculatePayroll() {
        // Get all sales from database
        List<Sale> sales = DatabaseOperations.getAllSales();

        if (sales.isEmpty()) {
            resetDisplay();
            return;
        }

        // Calculate totals from sales
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCOGS = BigDecimal.ZERO;
        BigDecimal totalOpEx = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (Sale sale : sales) {
            totalRevenue = totalRevenue.add(sale.getNetRevenue());
            totalCOGS = totalCOGS.add(sale.getCostOfGoodsSold());
            totalOpEx = totalOpEx.add(sale.getOperatingExpenses());
            totalProfit = totalProfit.add(sale.getProfit());
        }

        // Calculate payroll based on selected mode
        BigDecimal payrollCost;

        if (percentageRadio.isSelected()) {
            try {
                BigDecimal percentage = new BigDecimal(payrollPercentageField.getText());
                payrollCost = totalRevenue.multiply(percentage)
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            } catch (NumberFormatException e) {
                payrollCost = BigDecimal.ZERO;
            }
        } else {
            try {
                payrollCost = new BigDecimal(fixedPayrollField.getText());
            } catch (NumberFormatException e) {
                payrollCost = BigDecimal.ZERO;
            }
        }

        // Calculate profit after payroll
        BigDecimal profitAfterPayroll = totalProfit.subtract(payrollCost);

        // Calculate payroll rate (what % of revenue it represents)
        BigDecimal payrollRate = BigDecimal.ZERO;
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            payrollRate = payrollCost.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        // Update all labels
        totalRevenueLabel.setText(String.format("$%,.2f", totalRevenue));
        calculatedPayrollLabel.setText(String.format("$%,.2f", payrollCost));
        payrollRateLabel.setText(String.format("%.2f%%", payrollRate));
        profitBeforePayrollLabel.setText(String.format("$%,.2f", totalProfit));
        profitAfterPayrollLabel.setText(String.format("$%,.2f", profitAfterPayroll));

        // Color code profit before payroll
        if (totalProfit.compareTo(BigDecimal.ZERO) >= 0) {
            profitBeforePayrollLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #10b981;");
        } else {
            profitBeforePayrollLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ef4444;");
        }

        // Color code profit after payroll
        if (profitAfterPayroll.compareTo(BigDecimal.ZERO) >= 0) {
            profitAfterPayrollLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #10b981;");
        } else {
            profitAfterPayrollLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ef4444;");
        }

        // Update chart
        updateChart(totalRevenue, totalCOGS, totalOpEx, payrollCost, profitAfterPayroll);
    }

    private void updateChart(BigDecimal revenue, BigDecimal cogs, BigDecimal opex,
                             BigDecimal payroll, BigDecimal profit) {
        breakdownChart.getData().clear();

        if (revenue.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // Only show positive values in pie chart
        if (cogs.compareTo(BigDecimal.ZERO) > 0) {
            PieChart.Data cogsSlice = new PieChart.Data(
                    String.format("COGS: $%,.2f (%.1f%%)",
                            cogs,
                            cogs.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))),
                    cogs.doubleValue()
            );
            breakdownChart.getData().add(cogsSlice);
        }

        if (opex.compareTo(BigDecimal.ZERO) > 0) {
            PieChart.Data opexSlice = new PieChart.Data(
                    String.format("Operating Expenses: $%,.2f (%.1f%%)",
                            opex,
                            opex.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))),
                    opex.doubleValue()
            );
            breakdownChart.getData().add(opexSlice);
        }

        if (payroll.compareTo(BigDecimal.ZERO) > 0) {
            PieChart.Data payrollSlice = new PieChart.Data(
                    String.format("Payroll: $%,.2f (%.1f%%)",
                            payroll,
                            payroll.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))),
                    payroll.doubleValue()
            );
            breakdownChart.getData().add(payrollSlice);
        }

        if (profit.compareTo(BigDecimal.ZERO) > 0) {
            PieChart.Data profitSlice = new PieChart.Data(
                    String.format("Net Profit: $%,.2f (%.1f%%)",
                            profit,
                            profit.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))),
                    profit.doubleValue()
            );
            breakdownChart.getData().add(profitSlice);
        } else if (profit.compareTo(BigDecimal.ZERO) < 0) {
            // Show loss as text in the chart
            PieChart.Data lossSlice = new PieChart.Data(
                    String.format("Loss: $%,.2f", profit.abs()),
                    0.1  // Tiny sliver just to show it exists
            );
            breakdownChart.getData().add(lossSlice);
        }
    }

    private void resetDisplay() {
        totalRevenueLabel.setText("$0.00");
        calculatedPayrollLabel.setText("$0.00");
        payrollRateLabel.setText("0.00%");
        profitBeforePayrollLabel.setText("$0.00");
        profitAfterPayrollLabel.setText("$0.00");
        breakdownChart.getData().clear();
    }
}