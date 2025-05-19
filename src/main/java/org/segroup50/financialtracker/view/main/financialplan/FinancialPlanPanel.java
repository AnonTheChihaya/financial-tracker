package org.segroup50.financialtracker.view.main.financialplan;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.BudgetPlanDao;
import org.segroup50.financialtracker.data.dao.SavingsGoalDao;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.BudgetPlan;
import org.segroup50.financialtracker.data.model.SavingsGoal;
import org.segroup50.financialtracker.data.model.Account;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FinancialPlanPanel extends JPanel {
    private SavingsGoalDao savingsGoalDao;
    private BudgetPlanDao budgetPlanDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    private JPanel savingsGoalPanel;
    private JPanel budgetPlanPanel;
    private JButton editSavingsGoalButton;
    private JButton editBudgetPlanButton;
    private JButton refreshButton;

    public FinancialPlanPanel() {
        savingsGoalDao = new SavingsGoalDao();
        budgetPlanDao = new BudgetPlanDao();
        accountDao = new AccountDao();
        transactionDao = new TransactionDao();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel titleLabel = new JLabel("Financial Plan");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createHorizontalGlue());

        // Add refresh button to header
        refreshButton = new JButton("Refresh");
        refreshButton.setForeground(new Color(55, 90, 129));
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.addActionListener(e -> refreshFinancialData());
        headerPanel.add(refreshButton);

        add(headerPanel);
        add(Box.createVerticalStrut(20));

        // Savings Goal Section
        JPanel savingsTitlePanel = new JPanel();
        savingsTitlePanel.setLayout(new BoxLayout(savingsTitlePanel, BoxLayout.X_AXIS));
        savingsTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel savingsTitleLabel = new JLabel("Savings Goal");
        savingsTitleLabel.setFont(savingsTitleLabel.getFont().deriveFont(Font.BOLD, 16));
        savingsTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        savingsTitlePanel.add(savingsTitleLabel);

        savingsTitlePanel.add(Box.createHorizontalGlue());

        editSavingsGoalButton = new JButton("Edit");
        editSavingsGoalButton.setForeground(new Color(55, 90, 129));
        editSavingsGoalButton.setBorderPainted(false);
        editSavingsGoalButton.setContentAreaFilled(false);
        editSavingsGoalButton.addActionListener(e -> editSavingsGoal());
        savingsTitlePanel.add(editSavingsGoalButton);

        add(savingsTitlePanel);
        add(Box.createVerticalStrut(10));

        savingsGoalPanel = new JPanel();
        savingsGoalPanel.setLayout(new BoxLayout(savingsGoalPanel, BoxLayout.Y_AXIS));
        savingsGoalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(savingsGoalPanel);

        add(Box.createVerticalStrut(30));

        // Budget Plan Section
        JPanel budgetTitlePanel = new JPanel();
        budgetTitlePanel.setLayout(new BoxLayout(budgetTitlePanel, BoxLayout.X_AXIS));
        budgetTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel budgetTitleLabel = new JLabel("Budget Plan");
        budgetTitleLabel.setFont(budgetTitleLabel.getFont().deriveFont(Font.BOLD, 16));
        budgetTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        budgetTitlePanel.add(budgetTitleLabel);

        budgetTitlePanel.add(Box.createHorizontalGlue());

        editBudgetPlanButton = new JButton("Edit");
        editBudgetPlanButton.setForeground(new Color(55, 90, 129));
        editBudgetPlanButton.setBorderPainted(false);
        editBudgetPlanButton.setContentAreaFilled(false);
        editBudgetPlanButton.addActionListener(e -> editBudgetPlan());
        budgetTitlePanel.add(editBudgetPlanButton);

        add(budgetTitlePanel);
        add(Box.createVerticalStrut(10));

        budgetPlanPanel = new JPanel();
        budgetPlanPanel.setLayout(new BoxLayout(budgetPlanPanel, BoxLayout.Y_AXIS));
        budgetPlanPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(budgetPlanPanel);

        // Load data
        loadFinancialPlanData();
    }

    private void refreshFinancialData() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();

        // Refresh savings goal data
        List<SavingsGoal> savingsGoals = savingsGoalDao.getSavingsGoalsByUserId(userId);
        if (!savingsGoals.isEmpty()) {
            SavingsGoal goal = savingsGoals.get(0);
            // Recalculate current amount from accounts
            double totalBalance = accountDao.getAccountsByUserId(userId).stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            goal.setCurrentAmount(totalBalance);
            savingsGoalDao.updateSavingsGoal(goal);
        }

        // Refresh budget plans data
        List<BudgetPlan> budgetPlans = budgetPlanDao.getBudgetPlansByUserId(userId);
        for (BudgetPlan plan : budgetPlans) {
            // Recalculate spent amount
            double spentAmount = transactionDao.getTransactionsByUserId(userId).stream()
                    .filter(t -> t.getType().equalsIgnoreCase("Expense"))
                    .filter(t -> plan.getCategory() == null || plan.getCategory().isEmpty() ||
                            t.getCategory().equalsIgnoreCase(plan.getCategory()))
                    .filter(t -> {
                        if (plan.getStartDate() == null || plan.getEndDate() == null) return false;
                        return t.getDate().compareTo(plan.getStartDate().toString()) >= 0 &&
                                t.getDate().compareTo(plan.getEndDate().toString()) <= 0;
                    })
                    .mapToDouble(t -> Math.abs(t.getAmount()))
                    .sum();
            plan.setSpentAmount(spentAmount);
            budgetPlanDao.updateBudgetPlan(plan);
        }

        // Reload the UI with updated data
        loadFinancialPlanData();

        JOptionPane.showMessageDialog(this, "Financial data refreshed successfully!",
                "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadFinancialPlanData() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();

        // Load savings goal
        List<SavingsGoal> savingsGoals = savingsGoalDao.getSavingsGoalsByUserId(userId);
        savingsGoalPanel.removeAll();

        if (savingsGoals.isEmpty()) {
            JLabel noGoalLabel = new JLabel("No savings goal set");
            noGoalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            savingsGoalPanel.add(noGoalLabel);
        } else {
            SavingsGoal goal = savingsGoals.get(0); // Assuming one goal per user for now
            addSavingsGoalCard(goal);
        }

        // Load budget plans
        List<BudgetPlan> budgetPlans = budgetPlanDao.getBudgetPlansByUserId(userId);
        budgetPlanPanel.removeAll();

        if (budgetPlans.isEmpty()) {
            JLabel noBudgetLabel = new JLabel("No budget plan set");
            noBudgetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            budgetPlanPanel.add(noBudgetLabel);
        } else {
            for (BudgetPlan plan : budgetPlans) {
                addBudgetPlanCard(plan);
                budgetPlanPanel.add(Box.createVerticalStrut(15));
            }
        }

        savingsGoalPanel.revalidate();
        savingsGoalPanel.repaint();
        budgetPlanPanel.revalidate();
        budgetPlanPanel.repaint();
    }

    private void addSavingsGoalCard(SavingsGoal goal) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Goal description
        JLabel descriptionLabel = new JLabel(goal.getDescription());
        descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(Font.BOLD, 14));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(10));

        // Target date
        JLabel dateLabel = new JLabel("Target Date: " + goal.getTargetDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(dateLabel);
        card.add(Box.createVerticalStrut(5));

        // Progress bar
        int progress = goal.getProgressPercentage();
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(5));

        // Amounts
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.X_AXIS));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentLabel = new JLabel("Saved: $" + String.format("%.2f", goal.getCurrentAmount()));
        currentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountPanel.add(currentLabel);

        amountPanel.add(Box.createHorizontalGlue());

        JLabel targetLabel = new JLabel("Target: $" + String.format("%.2f", goal.getTargetAmount()));
        targetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        amountPanel.add(targetLabel);

        card.add(amountPanel);

        savingsGoalPanel.add(card);
    }

    private void addBudgetPlanCard(BudgetPlan plan) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Plan title
        String title = plan.getCategory() != null && !plan.getCategory().isEmpty() ?
                plan.getCategory() + " Budget" : "Overall Budget";
        JLabel titleLabel = new JLabel(title + " (" + plan.getPeriod() + ")");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));

        // Date range
        JLabel dateLabel = new JLabel(plan.getStartDate().format(DateTimeFormatter.ofPattern("MMM d")) + " - " +
                plan.getEndDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(dateLabel);
        card.add(Box.createVerticalStrut(5));

        // Progress bar
        int progress = plan.getProgressPercentage();
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(progress);
        progressBar.setStringPainted(true);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        progressBar.setForeground(progress > 100 ? Color.RED : new Color(55, 90, 129));
        card.add(progressBar);
        card.add(Box.createVerticalStrut(5));

        // Amounts
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.X_AXIS));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel spentLabel = new JLabel("Spent: $" + String.format("%.2f", plan.getSpentAmount()));
        spentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountPanel.add(spentLabel);

        amountPanel.add(Box.createHorizontalGlue());

        JLabel budgetLabel = new JLabel("Budget: $" + String.format("%.2f", plan.getBudgetAmount()));
        budgetLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        amountPanel.add(budgetLabel);

        card.add(amountPanel);

        // Remaining/Over budget
        double remaining = plan.getRemainingAmount();
        JLabel remainingLabel = new JLabel();
        remainingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (remaining >= 0) {
            remainingLabel.setText("Remaining: $" + String.format("%.2f", remaining));
            remainingLabel.setForeground(new Color(0, 180, 0));
        } else {
            remainingLabel.setText("Over budget: $" + String.format("%.2f", -remaining));
            remainingLabel.setForeground(Color.RED);
        }
        card.add(Box.createVerticalStrut(5));
        card.add(remainingLabel);

        budgetPlanPanel.add(card);
    }

    private void editSavingsGoal() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();
        List<SavingsGoal> goals = savingsGoalDao.getSavingsGoalsByUserId(userId);
        SavingsGoal goal = goals.isEmpty() ? new SavingsGoal() : goals.get(0);

        SavingsGoalDialog dialog = new SavingsGoalDialog(goal);
        SavingsGoal updatedGoal = dialog.showDialog();

        if (updatedGoal != null) {
            updatedGoal.setUserId(userId);

            // Calculate current amount from accounts
            double totalBalance = accountDao.getAccountsByUserId(userId).stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            updatedGoal.setCurrentAmount(totalBalance);

            if (goal.getId() == null) {
                savingsGoalDao.addSavingsGoal(updatedGoal);
            } else {
                savingsGoalDao.updateSavingsGoal(updatedGoal);
            }

            loadFinancialPlanData();
        }
    }

    private void editBudgetPlan() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();
        List<BudgetPlan> existingPlans = new ArrayList<>(budgetPlanDao.getBudgetPlansByUserId(userId));

        // Ensure all plans have dates
        for (BudgetPlan plan : existingPlans) {
            if (plan.getStartDate() == null || plan.getEndDate() == null) {
                LocalDate startDate = LocalDate.now();
                plan.setStartDate(startDate);
                plan.setEndDate(startDate.plusMonths(1)); // Default to one month
            }
        }

        BudgetPlanDialog dialog = new BudgetPlanDialog(existingPlans);
        List<BudgetPlan> updatedPlans = dialog.showDialog();

        // Handle the case when all plans were removed
        if (updatedPlans != null) {
            // Delete existing plans
            budgetPlanDao.getBudgetPlansByUserId(userId).forEach(plan ->
                    budgetPlanDao.deleteBudgetPlan(plan.getId()));

            // Only add back plans if the list is not empty
            if (!updatedPlans.isEmpty()) {
                for (BudgetPlan plan : updatedPlans) {
                    if (plan.getBudgetAmount() > 0) { // Only save valid plans
                        plan.setUserId(userId);

                        // Calculate spent amount
                        double spentAmount = transactionDao.getTransactionsByUserId(userId).stream()
                                .filter(t -> t.getType().equalsIgnoreCase("Expense"))
                                .filter(t -> plan.getCategory() == null || plan.getCategory().isEmpty() ||
                                        t.getCategory().equalsIgnoreCase(plan.getCategory()))
                                .filter(t -> {
                                    if (plan.getStartDate() == null || plan.getEndDate() == null) return false;
                                    return t.getDate().compareTo(plan.getStartDate().toString()) >= 0 &&
                                            t.getDate().compareTo(plan.getEndDate().toString()) <= 0;
                                })
                                .mapToDouble(t -> Math.abs(t.getAmount()))
                                .sum();
                        plan.setSpentAmount(spentAmount);

                        budgetPlanDao.addBudgetPlan(plan);
                    }
                }
            }

            loadFinancialPlanData();
        }
    }
}
