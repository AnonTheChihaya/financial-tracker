package org.segroup50.financialtracker.data.model;

public class Transaction {
    private String id;
    private String date;
    private double amount;
    private String type; // Income or Expense
    private String category;
    private String accountId;
    private String userId;
    private String note;

    public Transaction() {
    }

    public Transaction(String id, String date, double amount, String type, String category,
                       String accountId, String userId, String note) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.accountId = accountId;
        this.userId = userId;
        this.note = note;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", accountId='" + accountId + '\'' +
                ", userId='" + userId + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
