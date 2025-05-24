package org.segroup50.financialtracker.view.main.analysis;

import org.segroup50.financialtracker.config.CurrentUserConfig;
import org.segroup50.financialtracker.service.ai.FinancialAnalyzer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AnalysisPanel extends JPanel {
    private final FinancialAnalyzer analyzer;
    private JButton reportButton;
    private JButton anomalyButton;
    private JTextArea resultArea;
    private JProgressBar progressBar;

    public AnalysisPanel() {
        this.analyzer = new FinancialAnalyzer();
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("AI Financial Analysis");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20));

        // Description
        JLabel descLabel = new JLabel("<html>Get AI-powered insights into your finances. " +
                "Generate comprehensive reports or detect unusual spending patterns.</html>");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(descLabel);
        add(Box.createVerticalStrut(30));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        reportButton = new JButton("Generate Financial Report");
        reportButton.setBackground(new Color(70, 130, 180));
        reportButton.setForeground(Color.WHITE);
        reportButton.addActionListener(this::handleGenerateReport);
        buttonPanel.add(reportButton);

        buttonPanel.add(Box.createHorizontalStrut(15));

        anomalyButton = new JButton("Detect Anomalies");
        anomalyButton.setBackground(new Color(178, 34, 34));
        anomalyButton.setForeground(Color.WHITE);
        anomalyButton.addActionListener(this::handleDetectAnomalies);
        buttonPanel.add(anomalyButton);

        add(buttonPanel);
        add(Box.createVerticalStrut(20));

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
        progressBar.setVisible(false);
        add(progressBar);
        add(Box.createVerticalStrut(10));

        // Result area
        resultArea = new JTextArea();
        resultArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultArea.setBorder(BorderFactory.createTitledBorder("Analysis Results"));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane);
    }

    private void handleGenerateReport(ActionEvent e) {
        if (!checkAuthorization()) return;

        setLoadingState(true);
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return analyzer.generateFinancialReport(CurrentUserConfig.getCurrentUser().getId());
            }

            @Override
            protected void done() {
                try {
                    resultArea.setText(get());
                } catch (Exception ex) {
                    resultArea.setText("Error generating report: " + ex.getMessage());
                } finally {
                    setLoadingState(false);
                }
            }
        }.execute();
    }

    private void handleDetectAnomalies(ActionEvent e) {
        if (!checkAuthorization()) return;

        setLoadingState(true);
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return analyzer.detectAnomalies(CurrentUserConfig.getCurrentUser().getId());
            }

            @Override
            protected void done() {
                try {
                    resultArea.setText(get());
                } catch (Exception ex) {
                    resultArea.setText("Error detecting anomalies: " + ex.getMessage());
                } finally {
                    setLoadingState(false);
                }
            }
        }.execute();
    }

    private boolean checkAuthorization() {
        if (!CurrentUserConfig.isUserLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please login first",
                    "Authorization Required", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "<html>This will share your financial data with our AI system.<br>" +
                        "Do you authorize this analysis?</html>",
                "Data Authorization",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return confirm == JOptionPane.YES_OPTION;
    }

    private void setLoadingState(boolean loading) {
        reportButton.setEnabled(!loading);
        anomalyButton.setEnabled(!loading);
        progressBar.setIndeterminate(loading);
        progressBar.setVisible(loading);

        if (loading) {
            resultArea.setText("Processing your request... Please wait.");
        }
    }
}
