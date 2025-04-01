package org.segroup50.financialtracker.view.auth.login;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.UserDao;
import org.segroup50.financialtracker.data.model.User;
import org.segroup50.financialtracker.service.validation.user.UserValidation;
import org.segroup50.financialtracker.service.validation.ValidationResult;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final InputField usernameField;
    private final InputField passwordField;
    private final UserDao userDao;

    public LoginPanel() {
        // Initialize UserDao
        userDao = new UserDao();

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(titleLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Username field
        usernameField = new InputField("Username", false);
        gbc.ipady = 10;
        add(usernameField, gbc);

        // Password field
        passwordField = new InputField("Password", true);
        add(passwordField, gbc);

        gbc.ipady = 0;

        // Terms checkbox
        JCheckBox agreeCheckbox = new JCheckBox(
                "<html><body style='margin-left: 5px; width: 200px'>" +
                        "I have read and agree to the Terms of Service and Privacy Policy" +
                        "</body></html>");
        add(agreeCheckbox, gbc);

        // Continue button
        JButton continueButton = new JButton("Continue");
        continueButton.putClientProperty("JButton.buttonType", "roundRect");
        continueButton.setBackground(new Color(55, 90, 129));
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.ipady = 10;
        add(continueButton, gbc);

        continueButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Validate input format first
            ValidationResult validationResult = UserValidation.validateLogin(username, password);

            if (!validationResult.isValid()) {
                JOptionPane.showMessageDialog(this,
                        validationResult.getMessage(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check terms agreement
            if (!agreeCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(this,
                        "You must agree to the Terms of Service and Privacy Policy",
                        "Agreement Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Authenticate user
            boolean authenticationSuccessful = authenticateUser(username, password);

            if (authenticationSuccessful) {
                CurrentUserConfig.setCurrentUser(userDao.getUserByUsername(username));
                LoginFrame frame = (LoginFrame) SwingUtilities.getWindowAncestor(this);
                frame.showTotpPanel();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register button
        JButton registerButton = new JButton("Sign Up");
        registerButton.putClientProperty("JButton.buttonType", "roundRect");
        gbc.insets = new Insets(4, 0, 0, 0);
        gbc.ipady = 10;
        add(registerButton, gbc);

        registerButton.addActionListener(e -> {

        });
    }

    private boolean authenticateUser(String username, String password) {
        // Get user by username
        User user = userDao.getUserByUsername(username);

        // Check if user exists and password matches
        return user != null && user.getPwd().equals(password);
    }
}
