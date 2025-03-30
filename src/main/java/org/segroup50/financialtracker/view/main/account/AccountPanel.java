package org.segroup50.financialtracker.view.main.account;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.service.account.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AccountPanel extends JPanel {
    private DefaultTableModel model;
    private AccountService accountService;
    private JTable accountTable;

    public AccountPanel() {
        accountService = new AccountService();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Accounts");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10));

        // Create account table
        String[] columnNames = { "Account Name", "Account Type", "Account Attribute", "Current Balance" };

        model = new DefaultTableModel(new Object[][] {}, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 3:
                        return Double.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        loadAccountData();

        accountTable = new JTable(model);
        accountTable.setAutoCreateRowSorter(true);
        accountTable.setFillsViewportHeight(true);
        accountTable.setRowHeight(30);

        accountTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = accountTable.getSelectedRow();
                    if (row != -1) {
                        showAccountDetails(model, row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        add(scrollPane);

        add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton newAccountButton = new JButton("Add New Account");
        newAccountButton.setBackground(new Color(55, 90, 129));
        newAccountButton.setMaximumSize(new Dimension(160, 35));
        newAccountButton.setForeground(Color.WHITE);
        newAccountButton.setPreferredSize(new Dimension(newAccountButton.getPreferredSize().width, 35));
        newAccountButton.addActionListener(e -> {
            if (!CurrentUserConfig.isUserLoggedIn()) {
                JOptionPane.showMessageDialog(null, "Please login first",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account newAccount = AccountInputDialog.showAccountInputDialog(null);
            if (newAccount != null) {
                boolean success = accountService.addAccount(newAccount);
                if (success) {
                    loadAccountData();
                    JOptionPane.showMessageDialog(null, "Account added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add account",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBackground(new Color(200, 93, 89));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.setMaximumSize(new Dimension(160, 35));
        deleteAccountButton.setPreferredSize(new Dimension(deleteAccountButton.getPreferredSize().width, 35));
        deleteAccountButton.addActionListener(e -> deleteSelectedAccount());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setMaximumSize(new Dimension(100, 35));
        refreshButton.setPreferredSize(new Dimension(refreshButton.getPreferredSize().width, 35));
        refreshButton.addActionListener(e -> loadAccountData());

        buttonPanel.add(newAccountButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteAccountButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(refreshButton);
        add(buttonPanel);

        add(Box.createVerticalStrut(4));
    }

    private void deleteSelectedAccount() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Please login first",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an account to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Account> accounts = accountService.getUserAccounts();
        if (selectedRow >= accounts.size()) {
            JOptionPane.showMessageDialog(null, "Invalid account selection",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account selectedAccount = accounts.get(selectedRow);
        String accountName = selectedAccount.getName();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the account '" + accountName + "' and all its transactions?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean accountDeleted = accountService.deleteAccount(selectedAccount);
            if (accountDeleted) {
                loadAccountData();
                JOptionPane.showMessageDialog(null, "Account and all its transactions deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete account",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadAccountData() {
        model.setRowCount(0);

        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        List<Account> accounts = accountService.getUserAccounts();
        for (Account account : accounts) {
            model.addRow(new Object[] {
                    account.getName(),
                    account.getType(),
                    account.getAttribute(),
                    account.getBalance()
            });
        }
    }

    private void showAccountDetails(DefaultTableModel model, int row) {
        String accountName = (String) model.getValueAt(row, 0);
        String accountType = (String) model.getValueAt(row, 1);
        String accountAttribute = (String) model.getValueAt(row, 2);
        double currentBalance = (Double) model.getValueAt(row, 3);

        String message = String.format(
                "<html><body style='width: 300px'>" +
                        "<h3>Account Details</h3>" +
                        "<p><b>Account Name:</b> %s</p>" +
                        "<p><b>Account Type:</b> %s</p>" +
                        "<p><b>Account Attribute:</b> %s</p>" +
                        "<p><b>Current Balance:</b> %.2f</p>" +
                        "</body></html>",
                accountName, accountType, accountAttribute, currentBalance);

        JOptionPane.showMessageDialog(
                this,
                message,
                "Account Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
