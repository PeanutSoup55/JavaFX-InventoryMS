package com.example.javafx_inventoryms.objects;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Sale {
    private int id;

    // Core financial data
    private BigDecimal grossRevenue;
    private BigDecimal costOfGoodsSold;
    private BigDecimal operatingExpenses;
    private BigDecimal taxAmount;

    // Calculated fields
    private BigDecimal netRevenue;
    private BigDecimal profit;

    // Business tracking
    private String paymentMethod;
    private String invoiceNumber;
    private int employeeId;
    private Timestamp saleDate;

    // Constructor
    public Sale(int id, BigDecimal grossRevenue, BigDecimal costOfGoodsSold,
                BigDecimal operatingExpenses, BigDecimal taxAmount, Timestamp saleDate) {
        this.id = id;
        this.grossRevenue = grossRevenue;
        this.costOfGoodsSold = costOfGoodsSold;
        this.operatingExpenses = operatingExpenses;
        this.taxAmount = taxAmount;
        this.saleDate = saleDate;

        calculateMetrics();
    }

    // Simple constructor for basic sales
    public Sale(int id, BigDecimal grossRevenue, BigDecimal costOfGoodsSold, Timestamp saleDate) {
        this(id, grossRevenue, costOfGoodsSold, BigDecimal.ZERO, BigDecimal.ZERO, saleDate);
    }

    // Calculate derived values
    private void calculateMetrics() {
        // Net Revenue = Gross Revenue - Tax
        this.netRevenue = grossRevenue.subtract(taxAmount != null ? taxAmount : BigDecimal.ZERO);

        // Profit = Net Revenue - COGS - Operating Expenses
        this.profit = netRevenue
                .subtract(costOfGoodsSold != null ? costOfGoodsSold : BigDecimal.ZERO)
                .subtract(operatingExpenses != null ? operatingExpenses : BigDecimal.ZERO);
    }

    // Recalculate when values change
    public void recalculate() {
        calculateMetrics();
    }

    // Profit margin calculation
    public BigDecimal getProfitMargin() {
        if (netRevenue.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return profit.divide(netRevenue, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getGrossRevenue() { return grossRevenue; }
    public void setGrossRevenue(BigDecimal grossRevenue) {
        this.grossRevenue = grossRevenue;
        recalculate();
    }

    public BigDecimal getCostOfGoodsSold() { return costOfGoodsSold; }
    public void setCostOfGoodsSold(BigDecimal costOfGoodsSold) {
        this.costOfGoodsSold = costOfGoodsSold;
        recalculate();
    }

    public BigDecimal getOperatingExpenses() { return operatingExpenses; }
    public void setOperatingExpenses(BigDecimal operatingExpenses) {
        this.operatingExpenses = operatingExpenses;
        recalculate();
    }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        recalculate();
    }

    public BigDecimal getNetRevenue() { return netRevenue; }
    public BigDecimal getProfit() { return profit; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Timestamp getSaleDate() { return saleDate; }
    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return String.format("Sale #%s - Revenue: $%,.2f, Profit: $%,.2f",
                invoiceNumber != null ? invoiceNumber : id,
                netRevenue,
                profit);
    }
}