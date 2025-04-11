package org.segroup50.financialtracker.view.auth.login;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.service.utils.TotpValidator;
import org.segroup50.financialtracker.view.components.InputField;
import org.segroup50.financialtracker.view.main.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

/**
 * A custom JPanel that provides a TOTP verification interface for the
 * application.
 * This panel includes a TOTP token input field and a verification button.
 * The layout is designed using GridBagLayout for precise control.
 */
public class TotpInputPanel extends JPanel {

    private InputField totpField;

    /**
     * Constructs a new TotpInputPanel with all necessary UI components.
     */
    public TotpInputPanel() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Add title label
        JLabel titleLabel = new JLabel("Just One More Step...");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(titleLabel, gbc);

        // Reset constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Add instruction label
        JLabel instructionLabel = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "Open your <strong>TOTP Authenticator</strong> on your device and get the one-time password code"
                        +
                        "</div></html>",
                SwingConstants.CENTER);
        instructionLabel.setFont(instructionLabel.getFont().deriveFont(Font.PLAIN, 12));
        add(instructionLabel, gbc);

        // Add TOTP token input field
        gbc.ipady = 10;
        totpField = new InputField("TOTP Token", false, "Enter your 6-digit code");
        add(totpField, gbc);

        gbc.ipady = 0;

        // Add verify button
        JButton verifyButton = new JButton("Verify");
        verifyButton.putClientProperty("JButton.buttonType", "roundRect");
        verifyButton.setBackground(new Color(55, 90, 129));
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.ipady = 10;
        add(verifyButton, gbc);

        verifyButton.addActionListener(e -> {
            String totpCode = totpField.getText().trim();

            // Validate the TOTP code format
            if (totpCode.length() != 6 || !totpCode.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "Invalid TOTP code format. Please enter a 6-digit number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get current user's TOTP secret
            String totpSecret = CurrentUserConfig.getCurrentUser().getTotpsecret();

            // Validate the TOTP code
            if (TotpValidator.validateTotp(totpSecret, totpCode)) {
                // If valid, proceed to main frame
                Window currentFrame = SwingUtilities.getWindowAncestor(this);
                MainFrame mainFrame = new MainFrame();
                currentFrame.dispose();
                mainFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid TOTP code. Please try again.",
                        "Verification Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add back button
        JButton backButton = new JButton("Back");
        backButton.putClientProperty("JButton.buttonType", "roundRect");
        gbc.insets = new Insets(4, 0, 0, 0);
        gbc.ipady = 10;
        add(backButton, gbc);

        backButton.addActionListener(e -> {
            LoginFrame frame = (LoginFrame) SwingUtilities.getWindowAncestor(this);
            frame.showLoginPanel();
        });
    }
}
