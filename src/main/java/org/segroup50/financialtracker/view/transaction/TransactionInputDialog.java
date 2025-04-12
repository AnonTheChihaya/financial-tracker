package org.segroup50.financialtracker.view.main.transaction;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.model.Transaction;
import org.segroup50.financialtracker.service.validation.transaction.TransactionValidation;
import org.segroup50.financialtracker.service.validation.ValidationResult;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

/**
 * A custom dialog for inputting new transaction information.
 * The dialog includes fields for date, amount, type, category, account, and notes.
 */
public class TransactionInputDialog {
    public static Transaction showTransactionInputDialog(Component parentComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Transaction");
        dialog.setModal(true);
        dialog.setSize(400, 600);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialog.add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("Add New Transaction");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // In the InputField dateField section, modify it like this:
        InputField dateField = new InputField("Date (YYYY-MM-DD)", false);
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateField.getPreferredSize().height));
        // Add this line to set current date as default
        dateField.setText(java.time.LocalDate.now().toString());
        mainPanel.add(dateField);
        mainPanel.add(Box.createVerticalStrut(10));

        InputField amountField = new InputField("Amount", false);
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, amountField.getPreferredSize().height));
        mainPanel.add(amountField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Transaction Type (using JComboBox)
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));
        typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        typePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel typeLabel = new JLabel("Transaction Type");
        typeLabel.setFont(typeLabel.getFont().deriveFont(Font.PLAIN, 12));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        typePanel.add(typeLabel);
        typePanel.add(Box.createVerticalStrut(5));

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
        typeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, typeCombo.getPreferredSize().height));
        typePanel.add(typeCombo);

        mainPanel.add(typePanel);
        mainPanel.add(Box.createVerticalStrut(10));

        InputField categoryField = new InputField("Category", false);
        categoryField.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, categoryField.getPreferredSize().height));
        mainPanel.add(categoryField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Account selection (using JComboBox)
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel accountLabel = new JLabel("Account");
        accountLabel.setFont(accountLabel.getFont().deriveFont(Font.PLAIN, 12));
        accountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountPanel.add(accountLabel);
        accountPanel.add(Box.createVerticalStrut(5));

        // Get current user's accounts
        AccountDao accountDao = new AccountDao();
        String currentUserId = CurrentUserConfig.getCurrentUserId();
        List<Account> userAccounts = accountDao.getAccountsByUserId(currentUserId);

        // Create a combo box with account names and store account IDs as user data
        JComboBox<Account> accountCombo = new JComboBox<>();
        for (Account account : userAccounts) {
            accountCombo.addItem(account);
        }

        // Set renderer to display account names
        accountCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account account = (Account) value;
                    setText(account.getName() + " (" + account.getType() + ")");
                }
                return this;
            }
        });

        accountCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, accountCombo.getPreferredSize().height));
        accountPanel.add(accountCombo);

        mainPanel.add(accountPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        InputField notesField = new InputField("Notes", false);
        notesField.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesField.setMaximumSize(new Dimension(Integer.MAX_VALUE, notesField.getPreferredSize().height));
        mainPanel.add(notesField);
        mainPanel.add(Box.createVerticalStrut(20));

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

        // Create transaction reference to return
        final Transaction[] transaction = new Transaction[1];

        // Add button actions
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String amountText = amountField.getText();
                String type = (String) typeCombo.getSelectedItem();
                String category = categoryField.getText();
                Account selectedAccount = (Account) accountCombo.getSelectedItem();
                String note = notesField.getText();

                // Use TransactionValidation service for input validation
                ValidationResult validationResult =
                        TransactionValidation.validateTransactionInput(
                                date,
                                amountText,
                                type,
                                category,
                                selectedAccount != null ? selectedAccount.getId() : null,
                                note
                        );

                if (!validationResult.isValid()) {
                    JOptionPane.showMessageDialog(dialog, validationResult.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    // Note: userId will be set by the TransactionPanel after this dialog returns
                    transaction[0] = new Transaction(
                            UUID.randomUUID().toString(),
                            date,
                            type.equals("Expense") ? -Math.abs(amount) : Math.abs(amount),
                            type,
                            category,
                            selectedAccount.getId(), // Use the selected account's ID
                            null, // userId will be set later
                            note
                    );
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    // This shouldn't happen since we already validated the amount
                    JOptionPane.showMessageDialog(dialog, "Invalid amount format",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transaction[0] = null;
                dialog.dispose();
            }
        });

        // Center dialog on screen
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

        return transaction[0];
    }
}
