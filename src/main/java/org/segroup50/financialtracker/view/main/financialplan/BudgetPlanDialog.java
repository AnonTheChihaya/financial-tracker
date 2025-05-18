package org.segroup50.financialtracker.view.main.financialplan;

import org.segroup50.financialtracker.data.model.BudgetPlan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class BudgetPlanDialog {
    private JDialog dialog;
    private List<BudgetPlan> plans;
    private JComboBox<String> periodCombo;
    private JComboBox<String> categoryCombo;
    private JTextField budgetAmountField;
    private JButton addButton;
    private JPanel plansPanel;
    private JButton saveButton;

    public BudgetPlanDialog(List<BudgetPlan> existingPlans) {
        this.plans = new ArrayList<>(existingPlans);
        initialize();
    }

    private void initialize() {
        dialog = new JDialog();
        dialog.setTitle("Edit Budget Plans");
        dialog.setModal(true);
        dialog.setSize(500, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        dialog.add(mainPanel);

        // Title
        JLabel titleLabel = new JLabel("Budget Plans");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // New plan form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Budget Plan"));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Period selection
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        periodPanel.add(new JLabel("Period:"));
        periodCombo = new JComboBox<>(new String[]{"Weekly", "Monthly", "Yearly"});
        periodPanel.add(periodCombo);
        formPanel.add(periodPanel);

        // Category selection
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        categoryPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{"", "Food & Dining", "Groceries", "Transportation",
                "Housing", "Utilities", "Entertainment", "Shopping"});
        categoryCombo.setEditable(true);
        categoryPanel.add(categoryCombo);
        formPanel.add(categoryPanel);

        // Budget amount
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        amountPanel.add(new JLabel("Amount: $"));
        budgetAmountField = new JTextField(10);
        amountPanel.add(budgetAmountField);
        formPanel.add(amountPanel);

        // Add button
        addButton = new JButton("Add Plan");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(e -> addNewPlan());
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(addButton);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Existing plans
        plansPanel = new JPanel();
        plansPanel.setLayout(new BoxLayout(plansPanel, BoxLayout.Y_AXIS));
        plansPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshPlansPanel();
        mainPanel.add(plansPanel);

        // Save button
        saveButton = new JButton("Save All Plans");
        saveButton.setBackground(new Color(55, 90, 129));
        saveButton.setForeground(Color.WHITE);
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> dialog.dispose());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(saveButton);
    }

    private void addNewPlan() {
        try {
            String period = (String) periodCombo.getSelectedItem();
            String category = ((String) categoryCombo.getSelectedItem()).trim();
            double amount = Double.parseDouble(budgetAmountField.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(dialog, "Amount must be positive",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = calculateEndDate(startDate, period);

            BudgetPlan newPlan = new BudgetPlan();
            newPlan.setPeriod(period);
            newPlan.setCategory(category.isEmpty() ? null : category);
            newPlan.setBudgetAmount(amount);
            newPlan.setStartDate(startDate);
            newPlan.setEndDate(endDate);
            newPlan.setSpentAmount(0); // Initialize spent amount to 0

            plans.add(newPlan);
            refreshPlansPanel();

            // Reset form
            budgetAmountField.setText("");

            plansPanel.revalidate();
            plansPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dialog, "Invalid amount",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate calculateEndDate(LocalDate startDate, String period) {
        switch (period) {
            case "Weekly":
                return startDate.plusWeeks(1);
            case "Monthly":
                return startDate.plusMonths(1);
            case "Yearly":
                return startDate.plusYears(1);
            default:
                return startDate.plusMonths(1);
        }
    }

    private void refreshPlansPanel() {
        plansPanel.removeAll();
        plansPanel.setLayout(new BoxLayout(plansPanel, BoxLayout.X_AXIS));

        if (plans.isEmpty()) {
            JLabel noPlansLabel = new JLabel("No budget plans added yet");
            noPlansLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            plansPanel.add(noPlansLabel);
            return;
        }

        for (int i = 0; i < plans.size(); i++) {
            BudgetPlan plan = plans.get(i);
            // 只有当计划有有效数据时才显示
            if (plan.getBudgetAmount() > 0 ||
                    (plan.getCategory() != null && !plan.getCategory().isEmpty()) ||
                    (plan.getPeriod() != null && !plan.getPeriod().isEmpty())) {

                JPanel planPanel = new JPanel();
                planPanel.setLayout(new BoxLayout(planPanel, BoxLayout.Y_AXIS));
                planPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Plan " + (i + 1)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                planPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Period and category
                String period = plan.getPeriod() != null ? plan.getPeriod() : "Not set";
                String category = plan.getCategory() != null ? plan.getCategory() : "Overall";
                JLabel infoLabel = new JLabel(period + " - " + category);
                planPanel.add(infoLabel);

                // Date range
                if (plan.getStartDate() != null && plan.getEndDate() != null) {
                    JLabel dateLabel = new JLabel(
                            plan.getStartDate().format(DateTimeFormatter.ofPattern("MMM d")) +
                                    " to " +
                                    plan.getEndDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                    );
                    planPanel.add(dateLabel);
                } else {
                    planPanel.add(new JLabel("Date not set"));
                }

                // Budget amount
                JLabel amountLabel = new JLabel("Budget: $" + String.format("%.2f", plan.getBudgetAmount()));
                planPanel.add(amountLabel);

                // Remove button
                JButton removeButton = new JButton("Remove");
                int finalI = i;
                removeButton.addActionListener(e -> {
                    plans.remove(finalI);
                    refreshPlansPanel();
                    plansPanel.revalidate();
                    plansPanel.repaint();
                });
                planPanel.add(removeButton);

                plansPanel.add(planPanel);
                plansPanel.add(Box.createHorizontalStrut(10));
            }
        }
    }

    public List<BudgetPlan> showDialog() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        // Return empty list if all plans were removed
        return plans.isEmpty() ? new ArrayList<>() : plans;
    }

}