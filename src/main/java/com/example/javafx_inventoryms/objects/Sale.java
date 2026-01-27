package com.example.javafx_inventoryms.objects;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private double netRevenue;
    private double profit;
    private double payrollCost;
    private Timestamp saleDate;

    public Sale(int id, double netRevenue, double profit, double payrollCost, Timestamp saleDate) {
        this.id = id;
        this.netRevenue = netRevenue;
        this.profit = profit;
        this.payrollCost = payrollCost;
        this.saleDate = saleDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNetRevenue() {
        return netRevenue;
    }

    public void setNetRevenue(double netRevenue) {
        this.netRevenue = netRevenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getPayrollCost() {
        return payrollCost;
    }

    public void setPayrollCost(double payrollCost) {
        this.payrollCost = payrollCost;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
}
