package org.segroup50.financialtracker.view.main.transaction;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.model.Transaction;
import org.segroup50.financialtracker.service.validation.transaction.TransactionValidation;
import org.segroup50.financialtracker.service.validation.ValidationResult;
import org.segroup50.financialtracker.service.ai.TransactionImageProcessor;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransactionInputDialog {
    private static final String[] INCOME_CATEGORIES = {
            "Salary", "Bonus", "Investment", "Freelance", "Rental Income",
            "Gift", "Refund", "Dividend", "Interest", "Other Income"
    };

    private static final String[] EXPENSE_CATEGORIES = {
            "Food & Dining", "Groceries", "Transportation", "Housing", "Utilities",
            "Healthcare", "Insurance", "Entertainment", "Shopping", "Travel",
            "Education", "Personal Care", "Debt Payment", "Gifts & Donations",
            "Taxes", "Other Expense"
    };

    public static Transaction showTransactionInputDialog(Component parentComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Transaction");
        dialog.setModal(true);
        dialog.setSize(450, 700); // Increased height to accommodate import button

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

        // Import CSV button
        JButton importButton = new JButton("Import from CSV");
        importButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        importButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, importButton.getPreferredSize().height));
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCsvImport(dialog, parentComponent);
            }
        });
        mainPanel.add(importButton);
        mainPanel.add(Box.createVerticalStrut(15));

        // Date field
        InputField dateField = new InputField("Date (YYYY-MM-DD)", false);
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, dateField.getPreferredSize().height));
        dateField.setText(java.time.LocalDate.now().toString());
        mainPanel.add(dateField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Amount field
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

        // Category selection (using JComboBox with editable option)
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setFont(categoryLabel.getFont().deriveFont(Font.PLAIN, 12));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryPanel.add(categoryLabel);
        categoryPanel.add(Box.createVerticalStrut(5));

        // Initialize with income categories since "Income" is the default selection
        JComboBox<String> categoryCombo = new JComboBox<>(INCOME_CATEGORIES);
        categoryCombo.setEditable(true);
        categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, categoryCombo.getPreferredSize().height));

        // Update categories when transaction type changes
        typeCombo.addActionListener(e -> {
            String selectedType = (String) typeCombo.getSelectedItem();
            categoryCombo.removeAllItems();
            if ("Income".equals(selectedType)) {
                for (String category : INCOME_CATEGORIES) {
                    categoryCombo.addItem(category);
                }
            } else {
                for (String category : EXPENSE_CATEGORIES) {
                    categoryCombo.addItem(category);
                }
            }
        });

        categoryPanel.add(categoryCombo);
        mainPanel.add(categoryPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Account selection
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

        JComboBox<Account> accountCombo = new JComboBox<>();
        for (Account account : userAccounts) {
            accountCombo.addItem(account);
        }

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

        // Notes field
        InputField notesField = new InputField("Notes", false);
        notesField.setAlignmentX(Component.LEFT_ALIGNMENT);
        notesField.setMaximumSize(new Dimension(Integer.MAX_VALUE, notesField.getPreferredSize().height));
        mainPanel.add(notesField);
        mainPanel.add(Box.createVerticalStrut(20));

        // Upload image button (now placed just above the action buttons)
        JButton uploadButton = new JButton("Upload Payment Screenshot");
        uploadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        uploadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, uploadButton.getPreferredSize().height));
        mainPanel.add(uploadButton);
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

        // Add upload button action
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Payment Screenshot");
                int returnValue = fileChooser.showOpenDialog(dialog);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                        TransactionImageProcessor processor = new TransactionImageProcessor();
                        Map<String, String> details = processor.extractTransactionDetails(imageBytes);

                        // Update form fields with extracted data
                        dateField.setText(details.getOrDefault("date", java.time.LocalDate.now().toString()));
                        amountField.setText(details.getOrDefault("amount", ""));

                        String type = details.getOrDefault("type", "Expense");
                        typeCombo.setSelectedItem(type);

                        String category = details.getOrDefault("category",
                                "Income".equals(type) ? "Other Income" : "Other Expense");
                        categoryCombo.setSelectedItem(category);

                        notesField.setText(details.getOrDefault("notes", ""));

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                "Error processing image: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = dateField.getText();
                String amountText = amountField.getText();
                String type = (String) typeCombo.getSelectedItem();
                String category = (String) categoryCombo.getSelectedItem();
                Account selectedAccount = (Account) accountCombo.getSelectedItem();
                String note = notesField.getText();

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
                    transaction[0] = new Transaction(
                            UUID.randomUUID().toString(),
                            date,
                            type.equals("Expense") ? -Math.abs(amount) : Math.abs(amount),
                            type,
                            category,
                            selectedAccount.getId(),
                            null,
                            note
                    );
                    dialog.dispose();
                } catch (NumberFormatException ex) {
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

        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

        return transaction[0];
    }

    private static void handleCsvImport(JDialog parentDialog, Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File to Import");
        int returnValue = fileChooser.showOpenDialog(parentDialog);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                List<Transaction> transactions = parseCsvFile(selectedFile);
                if (transactions.isEmpty()) {
                    JOptionPane.showMessageDialog(parentDialog,
                            "No valid transactions found in the CSV file",
                            "Import Result", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Show confirmation dialog with import summary
                int confirm = JOptionPane.showConfirmDialog(parentDialog,
                        "Found " + transactions.size() + " valid transactions. Import them?",
                        "Confirm Import", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    TransactionDao transactionDao = new TransactionDao();
                    int successCount = 0;

                    for (Transaction transaction : transactions) {
                        if (transactionDao.addTransaction(transaction)) {
                            successCount++;
                        }
                    }

                    JOptionPane.showMessageDialog(parentDialog,
                            "Successfully imported " + successCount + " out of " + transactions.size() + " transactions",
                            "Import Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentDialog,
                        "Error reading CSV file: " + ex.getMessage(),
                        "Import Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static List<Transaction> parseCsvFile(File csvFile) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        String currentUserId = CurrentUserConfig.getCurrentUserId();
        AccountDao accountDao = new AccountDao();
        List<Account> userAccounts = accountDao.getAccountsByUserId(currentUserId);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }

                String[] values = line.split(",");
                if (values.length < 6) {
                    continue; // Skip invalid lines
                }

                try {
                    String date = values[0].trim();
                    double amount = Double.parseDouble(values[1].trim());
                    String type = values[2].trim();
                    String category = values[3].trim();
                    String accountName = values[4].trim();
                    String note = values.length > 5 ? values[5].trim() : "";

                    // Find matching account
                    Account matchingAccount = null;
                    for (Account account : userAccounts) {
                        if (account.getName().equalsIgnoreCase(accountName)) {
                            matchingAccount = account;
                            break;
                        }
                    }

                    if (matchingAccount == null) {
                        continue; // Skip if account not found
                    }

                    // Create transaction
                    Transaction transaction = new Transaction(
                            UUID.randomUUID().toString(),
                            date,
                            type.equalsIgnoreCase("expense") ? -Math.abs(amount) : Math.abs(amount),
                            type,
                            category,
                            matchingAccount.getId(),
                            currentUserId,
                            note
                    );

                    transactions.add(transaction);
                } catch (NumberFormatException e) {
                    continue; // Skip lines with invalid numbers
                }
            }
        }

        return transactions;
    }
}
