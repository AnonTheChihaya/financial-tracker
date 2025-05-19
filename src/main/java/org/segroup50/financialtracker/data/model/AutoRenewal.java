package org.segroup50.financialtracker.data.model;

public class AutoRenewal {
    private String id;
    private String userId;
    private String name;
    private double amount;
    private String period; // MONTH, QUARTER, YEAR
    private String accountId;
    private String note;

    public AutoRenewal() {
    }

    public AutoRenewal(String id, String userId, String name, double amount, String period, String accountId, String note) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.period = period;
        this.accountId = accountId;
        this.note = note;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "AutoRenewal{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", period='" + period + '\'' +
                ", accountId='" + accountId + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
