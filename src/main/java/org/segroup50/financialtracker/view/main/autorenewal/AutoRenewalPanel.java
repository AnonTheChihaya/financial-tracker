package org.segroup50.financialtracker.view.main.autorenewal;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.AutoRenewalDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.model.AutoRenewal;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

public class AutoRenewalPanel extends JPanel {
    private DefaultTableModel model;
    private JTable autoRenewalTable;
    private AutoRenewalDao autoRenewalDao;
    private AccountDao accountDao;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    public AutoRenewalPanel() {
        autoRenewalDao = new AutoRenewalDao();
        accountDao = new AccountDao();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Auto Renewal Management");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(20));

        // Create table
        String[] columnNames = {"Name", "Amount", "Period", "Account", "Note"};
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

        loadAutoRenewalData();

        autoRenewalTable = new JTable(model);
        autoRenewalTable.getColumnModel().getColumn(1).setCellRenderer(new AmountCellRenderer());
        autoRenewalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        autoRenewalTable.setFillsViewportHeight(true);
        autoRenewalTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(autoRenewalTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        add(scrollPane);

        add(Box.createVerticalStrut(20));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        addButton = new JButton("Add");
        addButton.setBackground(new Color(55, 90, 129));
        addButton.setForeground(Color.WHITE);
        addButton.setMaximumSize(new Dimension(120, 35));
        addButton.addActionListener(e -> handleAddAutoRenewal());

        editButton = new JButton("Edit");
        editButton.setBackground(new Color(55, 90, 129));
        editButton.setForeground(Color.WHITE);
        editButton.setMaximumSize(new Dimension(120, 35));
        editButton.addActionListener(e -> handleEditAutoRenewal());

        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(200, 93, 89));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setMaximumSize(new Dimension(120, 35));
        deleteButton.addActionListener(e -> handleDeleteAutoRenewal());

        buttonPanel.add(addButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(deleteButton);

        add(buttonPanel);
    }

    private void loadAutoRenewalData() {
        model.setRowCount(0);
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();
        List<AutoRenewal> autoRenewals = autoRenewalDao.getAutoRenewalsByUserId(userId);

        for (AutoRenewal autoRenewal : autoRenewals) {
            Account account = accountDao.getAccountById(autoRenewal.getAccountId());
            String accountName = account != null ? account.getName() : "Unknown Account";

            model.addRow(new Object[]{
                    autoRenewal.getName(),
                    autoRenewal.getAmount(),
                    autoRenewal.getPeriod(),
                    accountName,
                    autoRenewal.getNote()
            });
        }
    }

    private void handleAddAutoRenewal() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        AutoRenewal newAutoRenewal = AutoRenewalInputDialog.showAutoRenewalInputDialog(null);
        if (newAutoRenewal != null) {
            newAutoRenewal.setUserId(CurrentUserConfig.getCurrentUser().getId());
            boolean success = autoRenewalDao.addAutoRenewal(newAutoRenewal);
            if (success) {
                loadAutoRenewalData();
                showSuccess("Auto renewal added successfully!");
            } else {
                showError("Failed to add auto renewal");
            }
        }
    }

    private void handleEditAutoRenewal() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        int selectedRow = autoRenewalTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an auto renewal to edit");
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();
        List<AutoRenewal> autoRenewals = autoRenewalDao.getAutoRenewalsByUserId(userId);
        AutoRenewal selectedAutoRenewal = autoRenewals.get(selectedRow);

        AutoRenewal updatedAutoRenewal = AutoRenewalInputDialog.showAutoRenewalInputDialog(null, selectedAutoRenewal);
        if (updatedAutoRenewal != null) {
            boolean success = autoRenewalDao.updateAutoRenewal(updatedAutoRenewal);
            if (success) {
                loadAutoRenewalData();
                showSuccess("Auto renewal updated successfully!");
            } else {
                showError("Failed to update auto renewal");
            }
        }
    }

    private void handleDeleteAutoRenewal() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            showError("Please login first");
            return;
        }

        int selectedRow = autoRenewalTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an auto renewal to delete");
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();
        List<AutoRenewal> autoRenewals = autoRenewalDao.getAutoRenewalsByUserId(userId);
        String autoRenewalId = autoRenewals.get(selectedRow).getId();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this auto renewal?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = autoRenewalDao.deleteAutoRenewal(autoRenewalId);
            if (success) {
                loadAutoRenewalData();
                showSuccess("Auto renewal deleted successfully!");
            } else {
                showError("Failed to delete auto renewal");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
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

class AutoRenewalInputDialog {
    private static final String[] PERIOD_OPTIONS = {"MONTH", "QUARTER", "YEAR"};

    public static AutoRenewal showAutoRenewalInputDialog(Component parentComponent) {
        return showAutoRenewalInputDialog(parentComponent, null);
    }

    public static AutoRenewal showAutoRenewalInputDialog(Component parentComponent, AutoRenewal existingAutoRenewal) {
        JDialog dialog = new JDialog();
        dialog.setTitle(existingAutoRenewal == null ? "Add New Auto Renewal" : "Edit Auto Renewal");
        dialog.setModal(true);
        dialog.setSize(450, 500);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialog.add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel(existingAutoRenewal == null ? "Add New Auto Renewal" : "Edit Auto Renewal");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Name field
        InputField nameField = new InputField("Name", false);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Amount field
        InputField amountField = new InputField("Amount", false);
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, amountField.getPreferredSize().height));
        mainPanel.add(amountField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Period selection
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new BoxLayout(periodPanel, BoxLayout.Y_AXIS));
        periodPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel periodLabel = new JLabel("Period");
        periodLabel.setFont(periodLabel.getFont().deriveFont(Font.PLAIN, 12));
        periodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodPanel.add(periodLabel);
        periodPanel.add(Box.createVerticalStrut(5));

        JComboBox<String> periodCombo = new JComboBox<>(PERIOD_OPTIONS);
        periodCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        periodCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, periodCombo.getPreferredSize().height));
        periodPanel.add(periodCombo);

        mainPanel.add(periodPanel);
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

        // Populate fields if editing existing auto renewal
        if (existingAutoRenewal != null) {
            nameField.setText(existingAutoRenewal.getName());
            amountField.setText(String.valueOf(existingAutoRenewal.getAmount()));
            periodCombo.setSelectedItem(existingAutoRenewal.getPeriod());

            // Select the account
            for (int i = 0; i < accountCombo.getItemCount(); i++) {
                Account account = accountCombo.getItemAt(i);
                if (account.getId().equals(existingAutoRenewal.getAccountId())) {
                    accountCombo.setSelectedIndex(i);
                    break;
                }
            }

            notesField.setText(existingAutoRenewal.getNote());
        }

        // Create auto renewal reference to return
        final AutoRenewal[] autoRenewal = new AutoRenewal[1];

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String amountText = amountField.getText();
                String period = (String) periodCombo.getSelectedItem();
                Account selectedAccount = (Account) accountCombo.getSelectedItem();
                String note = notesField.getText();

                // Basic validation
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Name cannot be empty",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Amount must be positive",
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (existingAutoRenewal != null) {
                        // Editing existing auto renewal
                        existingAutoRenewal.setName(name);
                        existingAutoRenewal.setAmount(amount);
                        existingAutoRenewal.setPeriod(period);
                        existingAutoRenewal.setAccountId(selectedAccount.getId());
                        existingAutoRenewal.setNote(note);
                        autoRenewal[0] = existingAutoRenewal;
                    } else {
                        // Creating new auto renewal
                        autoRenewal[0] = new AutoRenewal(
                                UUID.randomUUID().toString(),
                                CurrentUserConfig.getCurrentUser().getId(),
                                name,
                                amount,
                                period,
                                selectedAccount.getId(),
                                note
                        );
                    }
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
                autoRenewal[0] = null;
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

        return autoRenewal[0];
    }
}
