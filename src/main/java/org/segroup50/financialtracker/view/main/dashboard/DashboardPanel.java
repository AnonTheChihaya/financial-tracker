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
    private JTable recentTransactionsTable;
    private JPanel accountsPanel;
    private JButton refreshButton;

    public DashboardPanel() {
        accountDao = new AccountDao();
        transactionDao = new TransactionDao();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create header panel with title and refresh button
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Add title label
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createHorizontalGlue());

        // Add refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(55, 90, 129));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setMaximumSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> loadDashboardData());
        headerPanel.add(refreshButton);

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
        add(Box.createVerticalStrut(20));

        // Recent Transactions Section
        JPanel transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel transactionsTitleLabel = new JLabel("Recent Transactions (Last 30 Days)");
        transactionsTitleLabel.setFont(transactionsTitleLabel.getFont().deriveFont(Font.BOLD, 16));
        transactionsTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        transactionsPanel.add(transactionsTitleLabel);

        add(transactionsTitleLabel);
        add(Box.createVerticalStrut(10));

        // Create transaction table
        String[] columnNames = { "Date", "Amount", "Type", "Category", "Account" };
        DefaultTableModel model = new DefaultTableModel(new Object[][] {}, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 1 ? Double.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTransactionsTable = new JTable(model);
        recentTransactionsTable.getColumnModel().getColumn(1).setCellRenderer(new AmountCellRenderer());
        recentTransactionsTable.setAutoCreateRowSorter(true);
        recentTransactionsTable.setFillsViewportHeight(true);
        recentTransactionsTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(recentTransactionsTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        transactionsPanel.add(scrollPane);

        add(transactionsPanel);
        add(Box.createVerticalStrut(20));

        // Accounts Section
        JPanel accountsTitlePanel = new JPanel();
        accountsTitlePanel.setLayout(new BoxLayout(accountsTitlePanel, BoxLayout.X_AXIS));
        accountsTitlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accountsTitleLabel = new JLabel("Your Accounts");
        accountsTitleLabel.setFont(accountsTitleLabel.getFont().deriveFont(Font.BOLD, 16));
        accountsTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountsTitlePanel.add(accountsTitleLabel);

        accountsTitlePanel.add(Box.createHorizontalGlue());

        JButton viewAllButton = new JButton("View All");
        viewAllButton.setForeground(new Color(55, 90, 129));
        viewAllButton.setBorderPainted(false);
        viewAllButton.setContentAreaFilled(false);
        viewAllButton.addActionListener(e -> {
            // TODO: Implement view all accounts functionality
            JOptionPane.showMessageDialog(this, "View all accounts functionality would go here");
        });
        accountsTitlePanel.add(viewAllButton);

        add(accountsTitlePanel);
        add(Box.createVerticalStrut(10));

        accountsPanel = new JPanel();
        accountsPanel.setLayout(new BoxLayout(accountsPanel, BoxLayout.Y_AXIS));
        accountsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(accountsPanel);

        // Load data
        loadDashboardData();
    }

    private void loadDashboardData() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            return;
        }

        // Show loading state
        refreshButton.setEnabled(false);
        refreshButton.setText("Refreshing...");

        // Use SwingWorker to load data in background
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String userId = CurrentUserConfig.getCurrentUser().getId();

                // Calculate total assets
                List<Account> accounts = accountDao.getAccountsByUserId(userId);
                double totalAssets = accounts.stream().mapToDouble(Account::getBalance).sum();

                // Load recent transactions (last 30 days)
                LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String startDate = thirtyDaysAgo.format(formatter);
                String endDate = LocalDate.now().format(formatter);

                List<Transaction> recentTransactions = transactionDao.getTransactionsByDateRange(startDate, endDate)
                        .stream()
                        .filter(t -> t.getUserId().equals(userId))
                        .limit(5) // Show only 5 most recent
                        .toList();

                // Load top 3 accounts
                List<Account> topAccounts = accounts.stream()
                        .sorted((a1, a2) -> Double.compare(a2.getBalance(), a1.getBalance()))
                        .limit(3)
                        .toList();

                // Update UI in event dispatch thread
                SwingUtilities.invokeLater(() -> {
                    // Update total assets
                    totalAssetsLabel.setText(String.format("$%.2f", totalAssets));

                    // Update transactions table
                    DefaultTableModel model = (DefaultTableModel) recentTransactionsTable.getModel();
                    model.setRowCount(0);

                    for (Transaction transaction : recentTransactions) {
                        String accountName = accountDao.getAccountById(transaction.getAccountId()).getName();
                        model.addRow(new Object[] {
                                transaction.getDate(),
                                transaction.getAmount(),
                                transaction.getType(),
                                transaction.getCategory(),
                                accountName
                        });
                    }

                    // Update accounts panel
                    accountsPanel.removeAll();
                    if (topAccounts.isEmpty()) {
                        JLabel noAccountsLabel = new JLabel("No accounts found");
                        noAccountsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        accountsPanel.add(noAccountsLabel);
                    } else {
                        for (Account account : topAccounts) {
                            JPanel accountCard = createAccountCard(account);
                            accountCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                            accountsPanel.add(accountCard);
                            accountsPanel.add(Box.createVerticalStrut(10));
                        }
                    }

                    accountsPanel.revalidate();
                    accountsPanel.repaint();
                });

                return null;
            }

            @Override
            protected void done() {
                refreshButton.setText("Refresh");
                refreshButton.setEnabled(true);
            }
        }.execute();
    }

    private JPanel createAccountCard(Account account) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle border
                g2.setColor(new Color(180, 180, 180)); // Light gray border
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Internal padding
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setOpaque(false); // Make panel transparent for rounded corners to show

        // Add a white background panel inside for content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Transparent background

        JPanel topRow = new JPanel();
        topRow.setLayout(new BoxLayout(topRow, BoxLayout.X_AXIS));
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setOpaque(false); // Transparent background

        JLabel nameLabel = new JLabel(account.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14));
        topRow.add(nameLabel);

        topRow.add(Box.createHorizontalGlue());

        JLabel typeLabel = new JLabel(account.getType());
        typeLabel.setFont(typeLabel.getFont().deriveFont(Font.PLAIN, 12));
        typeLabel.setForeground(Color.GRAY);
        topRow.add(typeLabel);

        contentPanel.add(topRow);
        contentPanel.add(Box.createVerticalStrut(5));

        JLabel balanceLabel = new JLabel(String.format("$%.2f", account.getBalance()));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 16));
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(balanceLabel);

        card.add(contentPanel);

        return card;
    }

    private static class AmountCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Double) {
                double amount = (Double) value;
                c.setForeground(amount < 0 ? Color.RED : new Color(0, 180, 0));
            }

            ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
            return c;
        }
    }
}
