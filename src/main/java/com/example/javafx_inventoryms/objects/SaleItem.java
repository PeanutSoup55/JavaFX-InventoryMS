package com.example.javafx_inventoryms.objects;

public class SaleItem {
    private int id;
    private int saleId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitCOG;
    private double unitPrice;
    private double totalPrice;

    public SaleItem(int productId, String productName, int quantity, double unitPrice, double unitCOG) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitCOG = unitCOG;
        this.totalPrice = quantity * unitPrice;
    }

    public SaleItem(int id, int saleId, int productId, String productName, int quantity, double unitPrice, double totalPrice) {
        this.id = id;
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public int getSaleId() { return saleId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * unitPrice;
    }
    public double getUnitPrice() { return unitPrice; }
    public double getUnitCOG() { return unitCOG; }
    public double getTotalCOG() { return quantity * unitCOG; }
    public double getTotalPrice() { return totalPrice; }
}