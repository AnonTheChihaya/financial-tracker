package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.BudgetPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class BudgetPlanDao extends BaseCsvDao<BudgetPlan> {
    private static final String CSV_FILE = "budget_plans.csv";
    private static final String CSV_HEADER = "id,userId,budgetAmount,spentAmount,period,startDate,endDate,category";

    private TransactionDao transactionDao;

    public BudgetPlanDao() {
        super(CSV_FILE, CSV_HEADER);
        this.transactionDao = new TransactionDao();
    }

    @Override
    protected String getId(BudgetPlan plan) {
        return plan.getId();
    }

    @Override
    protected String toCsvLine(BudgetPlan plan) {
        if (plan.getId() == null || plan.getId().isEmpty()) {
            plan.setId(UUID.randomUUID().toString());
        }

        if (plan.getUserId() == null || plan.getUserId().isEmpty()) {
            throw new IllegalArgumentException("Budget plan must have a userId");
        }

        return String.join(",",
                plan.getId(),
                plan.getUserId(),
                String.valueOf(plan.getBudgetAmount()),
                String.valueOf(plan.getSpentAmount()),
                plan.getPeriod(),
                plan.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                plan.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                plan.getCategory() != null ? plan.getCategory() : "");
    }

    @Override
    protected BudgetPlan fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 7) {
            return null;
        }
        try {
            double budgetAmount = Double.parseDouble(parts[2]);
            double spentAmount = Double.parseDouble(parts[3]);
            LocalDate startDate = LocalDate.parse(parts[5], DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate endDate = LocalDate.parse(parts[6], DateTimeFormatter.ISO_LOCAL_DATE);
            String category = parts.length > 7 ? parts[7] : "";

            return new BudgetPlan(parts[0], parts[1], budgetAmount, spentAmount,
                    parts[4], startDate, endDate, category);
        } catch (Exception e) {
            System.err.println("Error parsing budget plan: " + e.getMessage());
            return null;
        }
    }

    public boolean addBudgetPlan(BudgetPlan plan) {
        calculateSpentAmount(plan);
        return add(plan);
    }

    public List<BudgetPlan> getAllBudgetPlans() {
        List<BudgetPlan> plans = getAll();
        plans.forEach(this::calculateSpentAmount);
        return plans;
    }

    public BudgetPlan getBudgetPlanById(String id) {
        BudgetPlan plan = getAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (plan != null) {
            calculateSpentAmount(plan);
        }
        return plan;
    }

    public List<BudgetPlan> getBudgetPlansByUserId(String userId) {
        List<BudgetPlan> plans = getAll().stream()
                .filter(plan -> plan.getUserId().equals(userId))
                .toList();
        plans.forEach(this::calculateSpentAmount);
        return plans;
    }

    public List<BudgetPlan> getBudgetPlansByCategory(String userId, String category) {
        List<BudgetPlan> plans = getAll().stream()
                .filter(plan -> plan.getUserId().equals(userId) &&
                        (category == null || category.equals(plan.getCategory())))
                .toList();
        plans.forEach(this::calculateSpentAmount);
        return plans;
    }

    public boolean updateBudgetPlan(BudgetPlan updatedPlan) {
        calculateSpentAmount(updatedPlan);
        return update(updatedPlan, updatedPlan.getId());
    }

    public boolean deleteBudgetPlan(String id) {
        return delete(id);
    }

    private void calculateSpentAmount(BudgetPlan plan) {
        if (plan.getUserId() == null || plan.getStartDate() == null || plan.getEndDate() == null) {
            return;
        }

        double spentAmount = transactionDao.getTransactionsByUserId(plan.getUserId()).stream()
                .filter(t -> t.getType().equalsIgnoreCase("Expense"))
                .filter(t -> plan.getCategory() == null || plan.getCategory().isEmpty() ||
                        t.getCategory().equalsIgnoreCase(plan.getCategory()))
                .filter(t -> t.getDate().compareTo(plan.getStartDate().toString()) >= 0 &&
                        t.getDate().compareTo(plan.getEndDate().toString()) <= 0)
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();

        plan.setSpentAmount(spentAmount);
    }
}
