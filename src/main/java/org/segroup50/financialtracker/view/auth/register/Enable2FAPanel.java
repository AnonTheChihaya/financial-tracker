package org.segroup50.financialtracker.view.auth.register;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.segroup50.financialtracker.config.UserTotpTokenConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A custom JPanel that provides an interface for enabling TOTP 2FA.
 * This panel includes a QR code for setup and instructions.
 *
 * @author YourName
 * @version 1.0
 * @since 2025-04-10
 */
public class Enable2FAPanel extends JPanel {

    /**
     * Constructs a new Enable2FAPanel with all necessary UI components.
     * The panel includes:
     * - A title label
     * - Instruction text
     * - QR code for TOTP setup
     * - Additional information text
     * - Confirmation button
     */
    public Enable2FAPanel() {
        // Use GridBagLayout as the main layout
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Add margins

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0); // Add spacing between components

        // Add title label (centered)
        JLabel titleLabel = new JLabel("Enable 2FA");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(titleLabel, gbc);

        // Reset fill and insets for other components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);

        // Add description text
        JLabel descriptionLabel = new JLabel(
                "<html><body style='width: 240px; text-align: center'>" +
                        "Scan this QR code with your TOTP Authenticator app " +
                        "on your device to add this account" +
                        "</body></html>"
        );
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(descriptionLabel, gbc);

        // Add secret key label and text field
        JLabel secretLabel = new JLabel("<html><strong>Your Secret</strong></html>");
        secretLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 0, 2, 0);
        add(secretLabel, gbc);

        String secretKey = UserTotpTokenConfig.getTotpSecret(); // Extract from TOTP URI or generate
        JTextField secretTextField = new JTextField(secretKey);
        secretTextField.setEditable(false);
        secretTextField.setHorizontalAlignment(JTextField.CENTER);
        gbc.insets = new Insets(3, 20, 15, 20);
        add(secretTextField, gbc);
        gbc.insets = new Insets(5, 0, 15, 0);

        // Generate and add QR code
        String totpUri = "otpauth://totp/FinancialTracker?secret=" + secretKey;
        BufferedImage qrCodeImage = generateQRCode(totpUri, 200, 200);
        JLabel qrCodeLabel = new JLabel(new ImageIcon(qrCodeImage));
        gbc.insets = new Insets(15, 0, 15, 0); // Add more space around QR code
        add(qrCodeLabel, gbc);

        // Add additional information text
        JLabel infoLabel = new JLabel(
                "<html><body style='width: 240px; text-align: center'>" +
                        "Every time you log in, you will need to obtain a one-time verification code from your OTP app." +
                        "</body></html>"
        );
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(5, 0, 20, 0);
        add(infoLabel, gbc);

        // Add confirmation button
        JButton confirmButton = new JButton("I Have Finished");
        confirmButton.putClientProperty("JButton.buttonType", "roundRect");
        confirmButton.setBackground(new Color(55, 90, 129));
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.ipady = 10;
        add(confirmButton, gbc);

        // Add action listener for the button
        confirmButton.addActionListener(e -> {
            RegisterFrame frame = (RegisterFrame) SwingUtilities.getWindowAncestor(this);
            frame.showRegisterFinishedPanel();
        });
    }

    /**
     * Generates a QR code image from the given text.
     *
     * @param text The text to encode in the QR code
     * @param width The width of the QR code image
     * @param height The height of the QR code image
     * @return The generated QR code as a BufferedImage
     */
    private BufferedImage generateQRCode(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            // Return a blank image if QR code generation fails
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    }
}
