package org.segroup50.financialtracker.view.main.financialplan;

import org.segroup50.financialtracker.data.model.SavingsGoal;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SavingsGoalDialog {
    private JDialog dialog;
    private SavingsGoal goal;
    private InputField targetAmountField;
    private InputField targetDateField;
    private InputField descriptionField;

    public SavingsGoalDialog(SavingsGoal goal) {
        this.goal = goal;
        initialize();
    }

    private void initialize() {
        dialog = new JDialog();
        dialog.setTitle(goal.getId() == null ? "Add Savings Goal" : "Edit Savings Goal");
        dialog.setModal(true);
        dialog.setSize(400, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        dialog.add(mainPanel);

        // Description
        InputField descriptionField = new InputField("Description", false);
        descriptionField.setText(goal.getDescription() != null ? goal.getDescription() : "");
        mainPanel.add(descriptionField);
        mainPanel.add(Box.createVerticalStrut(10));
        this.descriptionField = descriptionField;

        // Target amount
        InputField targetAmountField = new InputField("Target Amount ($)", false);
        targetAmountField.setText(goal.getTargetAmount() > 0 ? String.valueOf(goal.getTargetAmount()) : "");
        mainPanel.add(targetAmountField);
        mainPanel.add(Box.createVerticalStrut(10));
        this.targetAmountField = targetAmountField;

        // Target date
        InputField targetDateField = new InputField("Target Date (YYYY-MM-DD)", false);
        targetDateField.setText(goal.getTargetDate() != null ?
                goal.getTargetDate().format(DateTimeFormatter.ISO_LOCAL_DATE) :
                LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        mainPanel.add(targetDateField);
        mainPanel.add(Box.createVerticalStrut(20));
        this.targetDateField = targetDateField;

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(55, 90, 129));
        okButton.setForeground(Color.WHITE);
        okButton.setMaximumSize(new Dimension(120, 35));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(200, 93, 89));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setMaximumSize(new Dimension(120, 35));

        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        okButton.addActionListener(e -> {
            try {
                double targetAmount = Double.parseDouble(targetAmountField.getText());
                LocalDate targetDate = LocalDate.parse(targetDateField.getText());
                String description = descriptionField.getText();

                if (targetAmount <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Target amount must be positive",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (targetDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(dialog, "Target date cannot be in the past",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                goal.setTargetAmount(targetAmount);
                goal.setTargetDate(targetDate);
                goal.setDescription(description);

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            goal = null;
            dialog.dispose();
        });
    }

    public SavingsGoal showDialog() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return goal;
    }
}