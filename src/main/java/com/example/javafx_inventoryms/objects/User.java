package com.example.javafx_inventoryms.objects;

public class User {
    private int id;
    private String username;
    private String password;
    private String position;
    private double pay;
//    private Blob image;
//    private String ImagePath;

    public User(int id, String username, String password, String position, double pay) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }
}
