package org.segroup50.financialtracker.view.main.transaction;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.service.transaction.TransactionService;
import org.segroup50.financialtracker.data.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionPanel extends JPanel {
    private DefaultTableModel model;
    private JTable transactionTable;
    private TransactionService transactionService;
    private List<Transaction> allTransactions;
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> accountFilter;
    private JTextField minAmountFilter;
    private JTextField maxAmountFilter;
    private JTextField startDateFilter;
    private JTextField endDateFilter;

    public TransactionPanel() {
        transactionService = new TransactionService();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10));

        // Create filter panel
        add(createFilterPanel());
        add(Box.createVerticalStrut(10));

        // Create transaction table
        String[] columnNames = { "Date", "Amount", "Type", "Category", "Account", "Notes" };
        model = new DefaultTableModel(new Object[][] {}, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 1 ? Double.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadTransactionData();

        transactionTable = new JTable(model);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(new AmountCellRenderer());
        transactionTable.setAutoCreateRowSorter(true);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setRowHeight(30);

        transactionTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = transactionTable.getSelectedRow();
                    if (row != -1) {
                        showTransactionDetails(row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        add(scrollPane);

        add(Box.createVerticalStrut(20));

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
        add(Box.createVerticalStrut(4));
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180)); // Fixed height

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchPanel.add(new JLabel("Search Notes:"));
        searchField = new JTextField(25);
        searchField.setPreferredSize(new Dimension(200, 25));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Apply");
        searchButton.setPreferredSize(new Dimension(80, 25));
        searchButton.addActionListener(e -> applyFilters());
        searchPanel.add(searchButton);

        filterPanel.add(searchPanel);

        // First row of filters
        JPanel filterRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterRow1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Type filter
        filterRow1.add(new JLabel("Type:"));
        typeFilter = new JComboBox<>(new String[] { "All", "Income", "Expense" });
        typeFilter.setPreferredSize(new Dimension(120, 25));
        typeFilter.addActionListener(e -> applyFilters());
        filterRow1.add(typeFilter);

        // Category filter
        filterRow1.add(Box.createHorizontalStrut(10));
        filterRow1.add(new JLabel("Category:"));
        categoryFilter = new JComboBox<>();
        categoryFilter.setPreferredSize(new Dimension(120, 25));
        categoryFilter.addItem("All");
        filterRow1.add(categoryFilter);

        filterPanel.add(filterRow1);

        // Second row of filters
        JPanel filterRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Account filter
        filterRow2.add(new JLabel("Account:"));
        accountFilter = new JComboBox<>();
        accountFilter.setPreferredSize(new Dimension(120, 25));
        accountFilter.addItem("All");
        filterRow2.add(accountFilter);

        // Amount range filter
        filterRow2.add(Box.createHorizontalStrut(10));
        filterRow2.add(new JLabel("Amount:"));
        minAmountFilter = new JTextField(8);
        minAmountFilter.setPreferredSize(new Dimension(60, 25));
        filterRow2.add(minAmountFilter);
        filterRow2.add(new JLabel("to"));
        maxAmountFilter = new JTextField(8);
        maxAmountFilter.setPreferredSize(new Dimension(60, 25));
        filterRow2.add(maxAmountFilter);

        filterPanel.add(filterRow2);

        // Third row of filters
        JPanel filterRow3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterRow3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Date range filter
        filterRow3.add(new JLabel("Date:"));
        startDateFilter = new JTextField(10);
        startDateFilter.setPreferredSize(new Dimension(80, 25));
        filterRow3.add(startDateFilter);
        filterRow3.add(new JLabel("to"));
        endDateFilter = new JTextField(10);
        endDateFilter.setPreferredSize(new Dimension(80, 25));
        filterRow3.add(endDateFilter);

        // Reset button
        filterRow3.add(Box.createHorizontalStrut(20));
        JButton resetButton = new JButton("Reset Filters");
        resetButton.setPreferredSize(new Dimension(120, 25));
        resetButton.addActionListener(e -> resetFilters());
        filterRow3.add(resetButton);

        filterPanel.add(filterRow3);

        return filterPanel;
    }

    private void loadTransactionData() {
        model.setRowCount(0);
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        allTransactions = transactionService.getUserTransactions();
        updateCategoryFilter();
        updateAccountFilter();
        applyFilters();
    }

    private void updateCategoryFilter() {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All");
        List<String> categories = allTransactions.stream()
                .map(Transaction::getCategory)
                .distinct()
                .collect(Collectors.toList());
        categories.forEach(categoryFilter::addItem);
    }

    private void updateAccountFilter() {
        accountFilter.removeAllItems();
        accountFilter.addItem("All");
        List<String> accounts = allTransactions.stream()
                .map(t -> transactionService.getAccountName(t.getAccountId()))
                .distinct()
                .collect(Collectors.toList());
        accounts.forEach(accountFilter::addItem);
    }

    private void applyFilters() {
        model.setRowCount(0);

        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) typeFilter.getSelectedItem();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String selectedAccount = (String) accountFilter.getSelectedItem();

        String minAmountText = minAmountFilter.getText();
        String maxAmountText = maxAmountFilter.getText();
        String startDate = startDateFilter.getText();
        String endDate = endDateFilter.getText();

        List<Transaction> filtered = allTransactions.stream()
                .filter(t -> searchText.isEmpty() ||
                        (t.getNote() != null && t.getNote().toLowerCase().contains(searchText)))
                .filter(t -> selectedType.equals("All") || t.getType().equalsIgnoreCase(selectedType))
                .filter(t -> selectedCategory.equals("All") || t.getCategory().equalsIgnoreCase(selectedCategory))
                .filter(t -> selectedAccount.equals("All") ||
                        transactionService.getAccountName(t.getAccountId()).equalsIgnoreCase(selectedAccount))
                .filter(t -> {
                    try {
                        if (!minAmountText.isEmpty()) {
                            double minAmount = Double.parseDouble(minAmountText);
                            if (t.getAmount() < minAmount)
                                return false;
                        }
                        if (!maxAmountText.isEmpty()) {
                            double maxAmount = Double.parseDouble(maxAmountText);
                            if (t.getAmount() > maxAmount)
                                return false;
                        }
                        return true;
                    } catch (NumberFormatException e) {
                        return true;
                    }
                })
                .filter(t -> {
                    if (startDate.isEmpty() || endDate.isEmpty())
                        return true;
                    return t.getDate().compareTo(startDate) >= 0 && t.getDate().compareTo(endDate) <= 0;
                })
                .collect(Collectors.toList());

        for (Transaction transaction : filtered) {
            model.addRow(new Object[] {
                    transaction.getDate(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getCategory(),
                    transactionService.getAccountName(transaction.getAccountId()),
                    transaction.getNote()
            });
        }
    }

    private void resetFilters() {
        searchField.setText("");
        typeFilter.setSelectedIndex(0);
        categoryFilter.setSelectedIndex(0);
        accountFilter.setSelectedIndex(0);
        minAmountFilter.setText("");
        maxAmountFilter.setText("");
        startDateFilter.setText("");
        endDateFilter.setText("");
        applyFilters();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton newTransactionButton = new JButton("Add New Transaction");
        newTransactionButton.setBackground(new Color(55, 90, 129));
        newTransactionButton.setForeground(Color.WHITE);
        newTransactionButton.setMaximumSize(new Dimension(160, 35));
        newTransactionButton.addActionListener(e -> handleAddTransaction());

        JButton deleteTransactionButton = new JButton("Delete Transaction");
        deleteTransactionButton.setBackground(new Color(200, 93, 89));
        deleteTransactionButton.setForeground(Color.WHITE);
        deleteTransactionButton.setMaximumSize(new Dimension(160, 35));
        deleteTransactionButton.addActionListener(e -> handleDeleteTransaction());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setMaximumSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> handleRefresh());

        buttonPanel.add(newTransactionButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteTransactionButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(refreshButton);
        return buttonPanel;
    }

    private void handleRefresh() {
        loadTransactionData();
    }

    private void handleAddTransaction() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        Transaction newTransaction = TransactionInputDialog.showTransactionInputDialog(null);
        if (newTransaction != null) {
            boolean success = transactionService.addTransaction(newTransaction);
            if (success) {
                loadTransactionData();
                showSuccess("Transaction added successfully!");
            } else {
                showError("Failed to add transaction");
            }
        }
    }

    private void handleDeleteTransaction() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a transaction to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this transaction?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = transactionService.deleteTransaction(selectedRow);
            if (success) {
                loadTransactionData();
                showSuccess("Transaction deleted successfully!");
            } else {
                showError("Failed to delete transaction");
            }
        }
    }

    private void showTransactionDetails(int row) {
        String date = (String) model.getValueAt(row, 0);
        double amount = (Double) model.getValueAt(row, 1);
        String type = (String) model.getValueAt(row, 2);
        String category = (String) model.getValueAt(row, 3);
        String accountName = (String) model.getValueAt(row, 4);
        String notes = (String) model.getValueAt(row, 5);

        String message = String.format(
                "<html><body style='width: 300px'>" +
                        "<h3>Transaction Details</h3>" +
                        "<p><b>Date:</b> %s</p>" +
                        "<p><b>Amount:</b> <span style='color:%s'>%.2f</span></p>" +
                        "<p><b>Type:</b> %s</p>" +
                        "<p><b>Category:</b> %s</p>" +
                        "<p><b>Account:</b> %s</p>" +
                        "<p><b>Notes:</b> %s</p>" +
                        "</body></html>",
                date,
                (amount < 0 ? "red" : "green"),
                amount,
                type,
                category,
                accountName,
                notes);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Transaction Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class AmountCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Double) {
                double amount = (Double) value;
                c.setForeground(amount < 0 ? Color.RED : new Color(0, 180, 0));
            }

            ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
            return c;
        }
    }
}
