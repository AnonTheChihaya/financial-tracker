package org.segroup50.financialtracker.view.auth.register;

import org.segroup50.financialtracker.config.UserTotpTokenConfig;
import org.segroup50.financialtracker.data.dao.UserDao;
import org.segroup50.financialtracker.data.model.User;
import org.segroup50.financialtracker.service.validation.user.UserValidation;
import org.segroup50.financialtracker.service.validation.ValidationResult;
import org.segroup50.financialtracker.view.auth.login.LoginFrame;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class RegisterPanel extends JPanel {
    private InputField usernameField;
    private InputField emailField;
    private InputField phoneField;
    private InputField passwordField;
    private InputField confirmPasswordField;
    private JCheckBox agreeCheckbox;
    private UserDao userDao;

    public RegisterPanel() {
        this.userDao = new UserDao();
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Add title label
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(titleLabel, gbc);

        // Reset fill and insets
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 4, 0);
        gbc.ipady = 10;

        // Initialize and add input fields
        usernameField = new InputField("Username", false);
        add(usernameField, gbc);

        emailField = new InputField("Email", false);
        add(emailField, gbc);

        phoneField = new InputField("Phone Number", false);
        add(phoneField, gbc);

        passwordField = new InputField("Password", true);
        add(passwordField, gbc);

        confirmPasswordField = new InputField("Confirm Password", true);
        add(confirmPasswordField, gbc);

        gbc.ipady = 0;

        // Add checkbox
        agreeCheckbox = new JCheckBox(
                "<html><body style='margin-left: 5px; width: 200px'>" +
                        "I have read and agree to the Terms of Service and Privacy Policy" +
                        "</body></html>"
        );
        add(agreeCheckbox, gbc);

        // Add continue button
        JButton continueButton = new JButton("Continue");
        continueButton.putClientProperty("JButton.buttonType", "roundRect");
        continueButton.setBackground(new Color(55, 90, 129));
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.ipady = 10;
        add(continueButton, gbc);

        continueButton.addActionListener(e -> {
            // Get input values
            String username = usernameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Validate inputs
            ValidationResult result = UserValidation.validateRegistration(
                    username, email, phone, password, confirmPassword);

            if (!result.isValid()) {
                JOptionPane.showMessageDialog(this,
                        result.getMessage(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if username already exists
            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username already taken. Please choose a different one.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if terms are agreed
            if (!agreeCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(this,
                        "You must agree to the Terms of Service and Privacy Policy",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate TOTP secret
            String totpSecret = UserTotpTokenConfig.getTotpSecret();

            // Create user object
            User user = new User(
                    UUID.randomUUID().toString(),
                    username,
                    email,
                    phone,
                    password,
                    totpSecret
            );


            // Save user to CSV
            boolean isSaved = userDao.addUser(user);
            if (!isSaved) {
                JOptionPane.showMessageDialog(this,
                        "Failed to register user. Please try again.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Proceed if validation passes
            RegisterFrame frame = (RegisterFrame) SwingUtilities.getWindowAncestor(this);
            frame.showTotpPanel();
        });

        // Add sign in button
        JButton loginButton = new JButton("Sign In");
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        gbc.insets = new Insets(4, 0, 0, 0);
        gbc.ipady = 10;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            Window currentFrame = SwingUtilities.getWindowAncestor(this);
            LoginFrame loginFrame = new LoginFrame();
            currentFrame.dispose();
            loginFrame.setVisible(true);
        });
    }

    private boolean isUsernameTaken(String username) {
        User existingUser = userDao.getUserByUsername(username);
        return existingUser != null;
    }
}
