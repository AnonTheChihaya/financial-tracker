package org.segroup50.financialtracker.view.auth.register;

import org.segroup50.financialtracker.config.UserTotpTokenConfig;
import org.segroup50.financialtracker.service.utils.TOTPSecretGenerator;
import org.segroup50.financialtracker.view.components.AboutDialog;
import org.segroup50.financialtracker.view.components.HelpDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * The main frame for the register interface of the Financial Tracker application.
 * This frame contains the register panel and a help menu with user manual option.
 * Uses CardLayout to switch between different panels.
 *
 * @author SEGroup50
 * @version 1.0
 * @since 2024-03-01
 */
public class RegisterFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private RegisterPanel registerPanel;
    private Enable2FAPanel enable2FAPanel;
    private RegisterFinishedPanel registerFinishedPanel;

    /**
     * Constructs a new RegisterFrame with default settings.
     * Initializes the window properties, adds the register panel,
     * and sets up the menu bar.
     */
    public RegisterFrame() {
        setTitle("Register");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(460, 680);
        setLocationRelativeTo(null); // Center the window

        // Create and set up the menu bar
        createMenuBar();

        UserTotpTokenConfig.setTotpSecret(generateTotpSecret());

        // Initialize CardLayout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize panels
        registerPanel = new RegisterPanel();
        enable2FAPanel = new Enable2FAPanel();
        registerFinishedPanel = new RegisterFinishedPanel();

        // Add panels to card panel with names
        cardPanel.add(registerPanel, "REGISTER");
        cardPanel.add(enable2FAPanel, "TOTP");
        cardPanel.add(registerFinishedPanel, "REGISTER_FINISHED");

        // Add card panel to frame
        add(cardPanel);

        try {
            // Set icon for this frame
            Image icon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/consume.png")));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setResizable(false); // Fix window size
    }

    /**
     * Creates and configures the menu bar for the register frame.
     * Currently, includes only a Help menu with User Manual option.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create Help menu
        JMenu helpMenu = new JMenu("Help");

        // Create Help Assistant menu item
        JMenuItem helpAssistantItem = new JMenuItem("Help Assistant");
        helpAssistantItem.addActionListener(e -> HelpDialog.showHelpDialog(this));
        helpMenu.add(helpAssistantItem);

        // Add separator
        helpMenu.addSeparator();

        // Create About menu item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> AboutDialog.showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        // Set the menu bar
        setJMenuBar(menuBar);
    }


    /**
     * Switches back to the register panel.
     * This can be called from the TOTP panel if needed.
     */
    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "REGISTER");
    }

    /**
     * Switches to the TOTP panel.
     * This can be called from the register panel if needed.
     */
    public void showTotpPanel() {
        cardLayout.show(cardPanel, "TOTP");
    }

    public void showRegisterFinishedPanel() {
        cardLayout.show(cardPanel, "REGISTER_FINISHED");
    }

    private String generateTotpSecret() {
        // Use TOTPSecretGenerator to generate a secure TOTP secret
        return TOTPSecretGenerator.generateSecret();
    }
}
