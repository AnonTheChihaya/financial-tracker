package org.segroup50.financialtracker.view.main;

import org.segroup50.financialtracker.view.components.AboutDialog;
import org.segroup50.financialtracker.view.main.account.AccountPanel;
import org.segroup50.financialtracker.view.main.dashboard.DashboardPanel;
import org.segroup50.financialtracker.view.main.profile.ProfilePanel;
import org.segroup50.financialtracker.view.main.transaction.TransactionPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MainFrame() {
        initComponents();
        setupFrame();
        createMenuBar();
        try {
            // Set icon for this frame
            Image icon = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/icons/consume.png")));
            setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        // Create main panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create vertical tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Make sure the tab area doesn't take too much space
        tabbedPane.setPreferredSize(new Dimension(150, tabbedPane.getPreferredSize().height));

        // Add tabs
        addTab("Dashboard", new DashboardPanel());
        addTab("Accounts", new AccountPanel());
        addTab("Transactions", new TransactionPanel());
        addTab("Profile", new ProfilePanel());

        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Set content pane
        setContentPane(mainPanel);
    }

    private void addTab(String title, JComponent component) {
        // Create a container panel with BorderLayout
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Make the component fill the available space
        component.setPreferredSize(new Dimension(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE));
        tabPanel.add(component, BorderLayout.CENTER);

        tabbedPane.addTab(title, tabPanel);
    }

    private void setupFrame() {
        setTitle("Financial Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null); // Center the window
    }

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
}
