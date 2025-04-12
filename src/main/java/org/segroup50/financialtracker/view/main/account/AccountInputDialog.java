package org.segroup50.financialtracker.view.main.account;

import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.service.validation.account.AccountValidation;
import org.segroup50.financialtracker.service.validation.ValidationResult;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A custom dialog for inputting new account information.
 * The dialog includes fields for account name, type, attribute, and balance.
 * The layout uses BoxLayout for vertical arrangement with consistent styling.
 */
public class AccountInputDialog {
    public static Account showAccountInputDialog(Component parentComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Account");
        dialog.setModal(true);
        dialog.setSize(400, 450);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialog.add(mainPanel);

        // Title label
        JLabel titleLabel = new JLabel("Add New Account");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Input fields
        InputField nameField = new InputField("Account Name", false);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        mainPanel.add(nameField);
        mainPanel.add(Box.createVerticalStrut(10));

        InputField typeField = new InputField("Account Type", false);
        typeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        typeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, typeField.getPreferredSize().height));
        mainPanel.add(typeField);
        mainPanel.add(Box.createVerticalStrut(10));

        // Account Attribute (using JComboBox in an InputField-like container)
        JPanel attributePanel = new JPanel();
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attributePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel attributeLabel = new JLabel("Account Attribute");
        attributeLabel.setFont(attributeLabel.getFont().deriveFont(Font.PLAIN, 12));
        attributeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        attributePanel.add(attributeLabel);
        attributePanel.add(Box.createVerticalStrut(5));

        JComboBox<String> attributeCombo = new JComboBox<>(new String[] { "Savings", "Investment" });
        attributeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        attributeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, attributeCombo.getPreferredSize().height));
        attributePanel.add(attributeCombo);

        mainPanel.add(attributePanel);
        mainPanel.add(Box.createVerticalStrut(10));

        InputField balanceField = new InputField("Current Balance", false);
        balanceField.setAlignmentX(Component.LEFT_ALIGNMENT);
        balanceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, balanceField.getPreferredSize().height));
        mainPanel.add(balanceField);
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

        // Create account reference to return
        final Account[] account = new Account[1];

        // Add button actions
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String type = typeField.getText();
                String attribute = (String) attributeCombo.getSelectedItem();
                String balanceText = balanceField.getText();

                // Validate input using the validation class
                ValidationResult validationResult = AccountValidation.validateAccountInput(name, type, balanceText);

                if (!validationResult.isValid()) {
                    JOptionPane.showMessageDialog(dialog, validationResult.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double balance = Double.parseDouble(balanceText);
                    // Note: userId will be set by the AccountPanel after this dialog returns
                    account[0] = new Account(null, null, name, type, attribute, balance);
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid number for balance",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                account[0] = null;
                dialog.dispose();
            }
        });

        // Center dialog on screen
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

        return account[0];
    }
}
