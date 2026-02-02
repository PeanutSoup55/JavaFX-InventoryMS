package com.example.javafx_inventoryms.objects;

public class User {
    private int id;
    private String username;
    private String password;
    private String rank;
    private double pay;

    public User(int id, String username, String password, String rank, double pay) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rank = rank;
        this.pay = pay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
}
