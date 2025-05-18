package org.segroup50.financialtracker.data.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetPlan {
    private String id;
    private String userId;
    private double budgetAmount;
    private double spentAmount;
    private String period; // Weekly, Monthly, Yearly
    private LocalDate startDate;
    private LocalDate endDate;
    private String category; // Optional: can be null for overall budget

    public BudgetPlan() {
    }

    public BudgetPlan(String id, String userId, double budgetAmount, double spentAmount,
                      String period, LocalDate startDate, LocalDate endDate, String category) {
        this.id = id;
        this.userId = userId;
        this.budgetAmount = budgetAmount;
        this.spentAmount = spentAmount;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
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

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Helper methods to format dates
    public String getFormattedStartDate() {
        return startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getFormattedEndDate() {
        return endDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Calculate progress percentage
    public int getProgressPercentage() {
        if (budgetAmount <= 0) return 0;
        return (int) ((spentAmount / budgetAmount) * 100);
    }

    // Calculate remaining amount
    public double getRemainingAmount() {
        return budgetAmount - spentAmount;
    }

    @Override
    public String toString() {
        return "BudgetPlan{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", budgetAmount=" + budgetAmount +
                ", spentAmount=" + spentAmount +
                ", period='" + period + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", category='" + category + '\'' +
                '}';
    }
}
