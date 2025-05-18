package org.segroup50.financialtracker.view.main.financialplan;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.SavingsGoalDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.model.SavingsGoal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class FinancialPlanPanel extends JPanel {
    private SavingsGoalDao savingsGoalDao;
    private AccountDao accountDao;

    private JPanel savingsGoalPanel;
    private JButton editSavingsGoalButton;
    private JButton refreshButton;

    public FinancialPlanPanel() {
        savingsGoalDao = new SavingsGoalDao();
        accountDao = new AccountDao();

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

        // Reload the UI with updated data
        loadFinancialPlanData();

        JOptionPane.showMessageDialog(this, "Savings data refreshed successfully!",
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

        savingsGoalPanel.revalidate();
        savingsGoalPanel.repaint();
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
}
