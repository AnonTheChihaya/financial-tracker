package org.segroup50.financialtracker.view.main.account;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.service.account.AccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class AccountPanel extends JPanel {
    private DefaultTableModel model;
    private AccountService accountService;
    private JTable accountTable;
    private List<Account> allAccounts;
    private JTextField searchField;
    private JComboBox<String> typeFilter;
    private JComboBox<String> attributeFilter;
    private JTextField minBalanceFilter;
    private JTextField maxBalanceFilter;

    public AccountPanel() {
        accountService = new AccountService();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("My Accounts");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(10));

        // Create filter panel
        add(createFilterPanel());
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
        accountTable.getColumnModel().getColumn(3).setCellRenderer(new BalanceCellRenderer());
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

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
        add(Box.createVerticalStrut(4));
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        searchPanel.add(new JLabel("Search Name:"));
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
        typeFilter = new JComboBox<>();
        typeFilter.addItem("All");
        typeFilter.setPreferredSize(new Dimension(120, 25));
        filterRow1.add(typeFilter);

        // Attribute filter
        filterRow1.add(Box.createHorizontalStrut(10));
        filterRow1.add(new JLabel("Attribute:"));
        attributeFilter = new JComboBox<>();
        attributeFilter.addItem("All");
        attributeFilter.setPreferredSize(new Dimension(120, 25));
        filterRow1.add(attributeFilter);

        filterPanel.add(filterRow1);

        // Second row of filters
        JPanel filterRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        filterRow2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Balance range filter
        filterRow2.add(new JLabel("Balance:"));
        minBalanceFilter = new JTextField(8);
        minBalanceFilter.setPreferredSize(new Dimension(60, 25));
        filterRow2.add(minBalanceFilter);
        filterRow2.add(new JLabel("to"));
        maxBalanceFilter = new JTextField(8);
        maxBalanceFilter.setPreferredSize(new Dimension(60, 25));
        filterRow2.add(maxBalanceFilter);

        // Reset button
        filterRow2.add(Box.createHorizontalStrut(20));
        JButton resetButton = new JButton("Reset Filters");
        resetButton.setPreferredSize(new Dimension(120, 25));
        resetButton.addActionListener(e -> resetFilters());
        filterRow2.add(resetButton);

        filterPanel.add(filterRow2);

        return filterPanel;
    }

    private void loadAccountData() {
        model.setRowCount(0);
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        allAccounts = accountService.getUserAccounts();
        updateTypeFilter();
        updateAttributeFilter();
        applyFilters();
    }

    private void updateTypeFilter() {
        typeFilter.removeAllItems();
        typeFilter.addItem("All");
        List<String> types = allAccounts.stream()
                .map(Account::getType)
                .distinct()
                .collect(Collectors.toList());
        types.forEach(typeFilter::addItem);
    }

    private void updateAttributeFilter() {
        attributeFilter.removeAllItems();
        attributeFilter.addItem("All");
        List<String> attributes = allAccounts.stream()
                .map(Account::getAttribute)
                .distinct()
                .collect(Collectors.toList());
        attributes.forEach(attributeFilter::addItem);
    }

    private void applyFilters() {
        model.setRowCount(0);

        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) typeFilter.getSelectedItem();
        String selectedAttribute = (String) attributeFilter.getSelectedItem();

        String minBalanceText = minBalanceFilter.getText();
        String maxBalanceText = maxBalanceFilter.getText();

        List<Account> filtered = allAccounts.stream()
                .filter(a -> searchText.isEmpty() || a.getName().toLowerCase().contains(searchText))
                .filter(a -> selectedType.equals("All") || a.getType().equalsIgnoreCase(selectedType))
                .filter(a -> selectedAttribute.equals("All") || a.getAttribute().equalsIgnoreCase(selectedAttribute))
                .filter(a -> {
                    try {
                        if (!minBalanceText.isEmpty()) {
                            double minBalance = Double.parseDouble(minBalanceText);
                            if (a.getBalance() < minBalance)
                                return false;
                        }
                        if (!maxBalanceText.isEmpty()) {
                            double maxBalance = Double.parseDouble(maxBalanceText);
                            if (a.getBalance() > maxBalance)
                                return false;
                        }
                        return true;
                    } catch (NumberFormatException e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        for (Account account : filtered) {
            model.addRow(new Object[] {
                    account.getName(),
                    account.getType(),
                    account.getAttribute(),
                    account.getBalance()
            });
        }
    }

    private void resetFilters() {
        searchField.setText("");
        typeFilter.setSelectedIndex(0);
        attributeFilter.setSelectedIndex(0);
        minBalanceFilter.setText("");
        maxBalanceFilter.setText("");
        applyFilters();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton newAccountButton = new JButton("Add New Account");
        newAccountButton.setBackground(new Color(55, 90, 129));
        newAccountButton.setForeground(Color.WHITE);
        newAccountButton.setMaximumSize(new Dimension(160, 35));
        newAccountButton.addActionListener(e -> handleAddAccount());

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBackground(new Color(200, 93, 89));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.setMaximumSize(new Dimension(160, 35));
        deleteAccountButton.addActionListener(e -> handleDeleteAccount());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setMaximumSize(new Dimension(100, 35));
        refreshButton.addActionListener(e -> handleRefresh());

        buttonPanel.add(newAccountButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteAccountButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(refreshButton);
        return buttonPanel;
    }

    private void handleRefresh() {
        loadAccountData();
    }

    private void handleAddAccount() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        Account newAccount = AccountInputDialog.showAccountInputDialog(null);
        if (newAccount != null) {
            boolean success = accountService.addAccount(newAccount);
            if (success) {
                loadAccountData();
                showSuccess("Account added successfully!");
            } else {
                showError("Failed to add account");
            }
        }
    }

    private void handleDeleteAccount() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an account to delete");
            return;
        }

        List<Account> accounts = accountService.getUserAccounts();
        if (selectedRow >= accounts.size()) {
            showError("Invalid account selection");
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
                showSuccess("Account and all its transactions deleted successfully!");
            } else {
                showError("Failed to delete account");
            }
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class BalanceCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Double) {
                double balance = (Double) value;
                c.setForeground(balance < 0 ? Color.RED : new Color(0, 180, 0));
            }

            ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
            return c;
        }
    }
}
