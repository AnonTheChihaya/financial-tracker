package org.segroup50.financialtracker.view.auth.login;

import org.segroup50.financialtracker.view.components.AboutDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * The main frame for the login interface of the Financial Tracker application.
 * This frame contains the login panel and a help menu with user manual option.
 *
 * @author SEGroup50
 * @version 1.0
 * @since 2024-03-01
 */
public class LoginFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private TotpInputPanel totpInputPanel;

    /**
     * Constructs a new LoginFrame with default settings.
     * Initializes the window properties, adds the login panel,
     * and sets up the menu bar.
     */
    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 560);
        setLocationRelativeTo(null); // Center the window

        // Create and set up the menu bar
        createMenuBar();
        // Initialize CardLayout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize panels
        loginPanel = new LoginPanel();
        totpInputPanel = new TotpInputPanel();

        // Add panels to card panel with names
        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(totpInputPanel, "TOTP");

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
     * Creates and configures the menu bar for the login frame.
     * Currently, includes only a Help menu with User Manual option.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Create Help menu
        JMenu helpMenu = new JMenu("Help");

        // Create About menu item
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> AboutDialog.showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        // Set the menu bar
        setJMenuBar(menuBar);
    }

    /**
     * Switches back to the login panel.
     * This can be called from the TOTP panel if needed.
     */
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "LOGIN");
    }

    /**
     * Switches to the TOTP panel.
     * This can be called from the login panel if needed.
     */
    public void showTotpPanel() {
        cardLayout.show(cardPanel, "TOTP");
    }
}
