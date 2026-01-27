package com.example.javafx_inventoryms.objects;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String station;
    private boolean isEmpty;

    public Product(int id, String name, double price, int quantity, String station, boolean isEmpty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.station = station;
        this.isEmpty = isEmpty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
