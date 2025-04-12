package org.segroup50.financialtracker.view.main.profile;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.UserDao;
import org.segroup50.financialtracker.data.model.User;
import org.segroup50.financialtracker.service.validation.user.UserValidation;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A custom JPanel that displays user profile information.
 * This panel includes username, email, phone number, register date,
 * and last login time fields that can be toggled between read-only and editable states.
 * The layout is simplified to use BoxLayout for vertical arrangement.
 */
public class ProfilePanel extends JPanel {
    private InputField usernameField;
    private InputField emailField;
    private InputField phoneField;
    private InputField lastLoginField;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;
    private UserDao userDao;

    /**
     * Constructs a new ProfilePanel with all necessary UI components.
     */
    public ProfilePanel() {
        userDao = new UserDao();

        // Use BoxLayout for simple vertical arrangement
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Add margins

        // Add title label (left-aligned)
        JLabel titleLabel = new JLabel("Profile");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);

        // Add vertical spacing
        add(Box.createVerticalStrut(20));

        // Get current user from config
        User currentUser = CurrentUserConfig.getCurrentUser();

        // Format current date/time for register and last login
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(formatter);

        // Create input fields with real user data
        usernameField = new InputField("Username", false);
        usernameField.setText(currentUser != null ? currentUser.getUsername() : "Not logged in");
        usernameField.setReadOnly(true);
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        add(usernameField);

        add(Box.createVerticalStrut(10));

        emailField = new InputField("Email", false);
        emailField.setText(currentUser != null ? currentUser.getEmail() : "N/A");
        emailField.setReadOnly(true);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, emailField.getPreferredSize().height));
        add(emailField);

        add(Box.createVerticalStrut(10));

        phoneField = new InputField("Phone Number", false);
        phoneField.setText(currentUser != null ? currentUser.getPhone() : "N/A");
        phoneField.setReadOnly(true);
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, phoneField.getPreferredSize().height));
        add(phoneField);

        add(Box.createVerticalStrut(10));

        lastLoginField = new InputField("Last Login Time", false);
        lastLoginField.setText(currentDateTime);
        lastLoginField.setReadOnly(true);
        lastLoginField.setAlignmentX(Component.LEFT_ALIGNMENT);
        lastLoginField.setMaximumSize(new Dimension(Integer.MAX_VALUE, lastLoginField.getPreferredSize().height));
        add(lastLoginField);

        // Add vertical spacing before buttons
        add(Box.createVerticalStrut(20));

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Add Edit button
        editButton = new JButton("Edit");
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(editButton.getPreferredSize().width, 35));
        editButton.addActionListener(e -> toggleEditMode(true));

        // Add Save button (initially hidden)
        saveButton = new JButton("Save");
        saveButton.setForeground(Color.WHITE);
        saveButton.setPreferredSize(new Dimension(saveButton.getPreferredSize().width, 35));
        saveButton.addActionListener(e -> saveProfileChanges());
        saveButton.setVisible(false);

        // Add Cancel button (initially hidden)
        cancelButton = new JButton("Cancel");
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(cancelButton.getPreferredSize().width, 35));
        cancelButton.addActionListener(e -> toggleEditMode(false));
        cancelButton.setVisible(false);

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        add(buttonPanel);

        // Add vertical spacing between buttons
        add(Box.createVerticalStrut(4));

        // Add Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(200, 93, 89));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        logoutButton.setPreferredSize(new Dimension(editButton.getPreferredSize().width, 35));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                // If user confirms, clear current user and exit the program
                if (response == JOptionPane.YES_OPTION) {
                    CurrentUserConfig.clearCurrentUser();
                    System.exit(0);
                }
            }
        });
        add(logoutButton);
    }

    /**
     * Toggles between edit and view modes.
     * @param enableEdit true to enable edit mode, false to disable
     */
    private void toggleEditMode(boolean enableEdit) {

        // Toggle field editability (except username and last login time)
        emailField.setReadOnly(!enableEdit);
        phoneField.setReadOnly(!enableEdit);

        // Toggle button visibility
        editButton.setVisible(!enableEdit);
        saveButton.setVisible(enableEdit);
        cancelButton.setVisible(enableEdit);

        // If canceling, reset fields to original values
        if (!enableEdit) {
            User currentUser = CurrentUserConfig.getCurrentUser();
            if (currentUser != null) {
                emailField.setText(currentUser.getEmail());
                phoneField.setText(currentUser.getPhone());
            }
        }
    }

    /**
     * Saves the profile changes to the database.
     */
    private void saveProfileChanges() {
        User currentUser = CurrentUserConfig.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get field values
        String email = emailField.getText();
        String phone = phoneField.getText();

        // Validate email format using UserValidation
        if (!email.matches(UserValidation.EMAIL_PATTERN)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address",
                    "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate phone number format using UserValidation
        if (!phone.matches(UserValidation.PHONE_PATTERN)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number (10-15 digits)",
                    "Invalid Phone", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update user object
        currentUser.setEmail(email);
        currentUser.setPhone(phone);

        // Save to database
        boolean success = userDao.updateUser(currentUser);
        if (success) {
            CurrentUserConfig.setCurrentUser(currentUser); // Update in config
            JOptionPane.showMessageDialog(this, "Profile updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            toggleEditMode(false);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
