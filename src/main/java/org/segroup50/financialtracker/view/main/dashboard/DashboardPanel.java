package org.segroup50.financialtracker.view.main.dashboard;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.data.dao.AccountDao;
import org.segroup50.financialtracker.data.dao.TransactionDao;
import org.segroup50.financialtracker.data.model.Account;
import org.segroup50.financialtracker.data.model.Transaction;
import org.segroup50.financialtracker.view.components.InputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardPanel extends JPanel {
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private JLabel totalAssetsLabel;

    public DashboardPanel() {
        accountDao = new AccountDao();
        transactionDao = new TransactionDao();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Add title label
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        add(headerPanel);
        add(Box.createVerticalStrut(20));

        // Total Assets Section
        JPanel assetsPanel = new JPanel();
        assetsPanel.setLayout(new BoxLayout(assetsPanel, BoxLayout.Y_AXIS));
        assetsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        assetsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel assetsTitleLabel = new JLabel("Total Assets");
        assetsTitleLabel.setFont(assetsTitleLabel.getFont().deriveFont(Font.BOLD, 16));
        assetsTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        assetsPanel.add(assetsTitleLabel);

        totalAssetsLabel = new JLabel("$0.00");
        totalAssetsLabel.setFont(totalAssetsLabel.getFont().deriveFont(Font.BOLD, 24));
        totalAssetsLabel.setForeground(new Color(0, 180, 0));
        totalAssetsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        assetsPanel.add(totalAssetsLabel);

        add(assetsPanel);

        // Load data
        loadDashboardData();
    }

    private void loadDashboardData() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        String userId = CurrentUserConfig.getCurrentUser().getId();

        // Calculate total assets
        List<Account> accounts = accountDao.getAccountsByUserId(userId);
        double totalAssets = accounts.stream().mapToDouble(Account::getBalance).sum();

        // Update total assets
        totalAssetsLabel.setText(String.format("$%.2f", totalAssets));
    }
}
