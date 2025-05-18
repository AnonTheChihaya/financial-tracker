package org.segroup50.financialtracker.data.dao;

import org.segroup50.financialtracker.data.BaseCsvDao;
import org.segroup50.financialtracker.data.model.SavingsGoal;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.model.Account;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SavingsGoalDao extends BaseCsvDao<SavingsGoal> {
    private static final String CSV_FILE = "savings_goals.csv";
    private static final String CSV_HEADER = "id,userId,targetAmount,currentAmount,targetDate,description";

    private AccountDao accountDao;

    public SavingsGoalDao() {
        super(CSV_FILE, CSV_HEADER);
        this.accountDao = new AccountDao();
    }

    @Override
    protected String getId(SavingsGoal goal) {
        return goal.getId();
    }

    @Override
    protected String toCsvLine(SavingsGoal goal) {
        if (goal.getId() == null || goal.getId().isEmpty()) {
            goal.setId(UUID.randomUUID().toString());
        }

        if (goal.getUserId() == null || goal.getUserId().isEmpty()) {
            throw new IllegalArgumentException("Savings goal must have a userId");
        }

        return String.join(",",
                goal.getId(),
                goal.getUserId(),
                String.valueOf(goal.getTargetAmount()),
                String.valueOf(goal.getCurrentAmount()),
                goal.getTargetDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                goal.getDescription() != null ? goal.getDescription() : "");
    }

    @Override
    protected SavingsGoal fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 5) {
            return null;
        }
        try {
            double targetAmount = Double.parseDouble(parts[2]);
            double currentAmount = Double.parseDouble(parts[3]);
            LocalDate targetDate = LocalDate.parse(parts[4], DateTimeFormatter.ISO_LOCAL_DATE);
            String description = parts.length > 5 ? parts[5] : "";

            return new SavingsGoal(parts[0], parts[1], targetAmount, currentAmount, targetDate, description);
        } catch (Exception e) {
            System.err.println("Error parsing savings goal: " + e.getMessage());
            return null;
        }
    }

    public boolean addSavingsGoal(SavingsGoal goal) {
        calculateCurrentAmount(goal);
        return add(goal);
    }

    public List<SavingsGoal> getAllSavingsGoals() {
        List<SavingsGoal> goals = getAll();
        goals.forEach(this::calculateCurrentAmount);
        return goals;
    }

    public SavingsGoal getSavingsGoalById(String id) {
        SavingsGoal goal = getAll().stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (goal != null) {
            calculateCurrentAmount(goal);
        }
        return goal;
    }

    public List<SavingsGoal> getSavingsGoalsByUserId(String userId) {
        List<SavingsGoal> goals = getAll().stream()
                .filter(goal -> goal.getUserId().equals(userId))
                .toList();
        goals.forEach(this::calculateCurrentAmount);
        return goals;
    }

    public boolean updateSavingsGoal(SavingsGoal updatedGoal) {
        calculateCurrentAmount(updatedGoal);
        return update(updatedGoal, updatedGoal.getId());
    }

    public boolean deleteSavingsGoal(String id) {
        return delete(id);
    }

    private void calculateCurrentAmount(SavingsGoal goal) {
        if (goal.getUserId() == null) {
            return;
        }

        double totalBalance = accountDao.getAccountsByUserId(goal.getUserId()).stream()
                .mapToDouble(Account::getBalance)
                .sum();

        goal.setCurrentAmount(totalBalance);
    }
}
