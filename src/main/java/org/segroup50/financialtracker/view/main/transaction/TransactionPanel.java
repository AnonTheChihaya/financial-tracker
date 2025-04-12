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

public class TransactionPanel extends JPanel {
    private DefaultTableModel model;
    private JTable transactionTable;
    private TransactionService transactionService;

    public TransactionPanel() {
        transactionService = new TransactionService();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10));

        // Create transaction table
        String[] columnNames = {"Date", "Amount", "Type", "Category", "Account", "Notes"};
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
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

    private void loadTransactionData() {
        model.setRowCount(0);
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        var transactions = transactionService.getUserTransactions();
        for (Transaction transaction : transactions) {
            model.addRow(new Object[]{
                    transaction.getDate(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getCategory(),
                    transactionService.getAccountName(transaction.getAccountId()),
                    transaction.getNote()
            });
        }
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
                JOptionPane.YES_NO_OPTION
        );

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
                notes
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Transaction Details",
                JOptionPane.INFORMATION_MESSAGE
        );
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
