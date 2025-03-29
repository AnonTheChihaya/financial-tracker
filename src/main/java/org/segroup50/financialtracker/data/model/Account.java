package org.segroup50.financialtracker.data.model;

public class Account {
    private String id;
    private String userId; // Added field for user reference
    private String name;
    private String type;
    private String attribute; // Saving or Investment
    private double balance;

    public Account() {
    }

    public Account(String id, String userId, String name, String type, String attribute, double balance) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.attribute = attribute;
        this.balance = balance;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", attribute='" + attribute + '\'' +
                ", balance=" + balance +
                '}';
    }
}
