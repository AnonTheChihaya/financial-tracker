package org.segroup50.financialtracker.view.auth.register;

import org.segroup50.financialtracker.view.auth.login.LoginFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A custom JPanel that displays a registration completion message.
 * This panel includes:
 * - A congratulations title
 * - A success message
 * - A button to return to login
 * The layout is designed using GridBagLayout for precise control.
 *
 * @author [Your Name]
 * @version 1.0
 * @since [Current Date]
 */
public class RegisterFinishedPanel extends JPanel {

    /**
     * Constructs a new RegisterFinishedPanel with all necessary UI components.
     * The panel includes:
     * - A centered congratulations label
     * - A success message
     * - A button to return to login
     * Components are properly aligned using GridBagLayout.
     */
    public RegisterFinishedPanel() {
        // Use GridBagLayout as the main layout
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Add margins

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0); // Add spacing between components

        // Add title label (centered)
        JLabel titleLabel = new JLabel("Congratulations");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        gbc.fill = GridBagConstraints.NONE; // Don't stretch the label
        gbc.insets = new Insets(0, 0, 20, 0); // Add more space below the title
        add(titleLabel, gbc);

        // Reset fill and insets for other components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Add success message
        JLabel successLabel = new JLabel(
                "<html><body style='text-align: center; width: 200px'>" +
                        "Account registration has been successfully completed." +
                        "</body></html>",
                SwingConstants.CENTER
        );
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(successLabel, gbc);

        // Reset anchor and fill for button
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add login button
        JButton loginButton = new JButton("Take me to Login");
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setBackground(new Color(55, 90, 129));
        gbc.insets = new Insets(15, 0, 0, 0); // Add more space above the button
        gbc.ipady = 10; // More space for height
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            Window currentFrame = SwingUtilities.getWindowAncestor(this);
            LoginFrame loginFrame = new LoginFrame();
            currentFrame.dispose();
            loginFrame.setVisible(true);
        });
    }
}
