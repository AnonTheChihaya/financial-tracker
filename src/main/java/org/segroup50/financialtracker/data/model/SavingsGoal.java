package org.segroup50.financialtracker.data.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SavingsGoal {
    private String id;
    private String userId;
    private double targetAmount;
    private double currentAmount;
    private LocalDate targetDate;
    private String description;

    public SavingsGoal() {
    }

    public SavingsGoal(String id, String userId, double targetAmount, double currentAmount,
                       LocalDate targetDate, String description) {
        this.id = id;
        this.userId = userId;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
        this.description = description;
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

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Helper method to format target date
    public String getFormattedTargetDate() {
        return targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Calculate progress percentage
    public int getProgressPercentage() {
        if (targetAmount <= 0) return 0;
        return (int) ((currentAmount / targetAmount) * 100);
    }

    @Override
    public String toString() {
        return "SavingsGoal{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", targetDate=" + targetDate +
                ", description='" + description + '\'' +
                '}';
    }
}
