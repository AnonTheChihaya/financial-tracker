package org.segroup50.financialtracker.view.components;

import org.segroup50.financialtracker.service.ai.AIAssistant;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HelpDialog extends JDialog {
    private final AIAssistant assistant;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JProgressBar progressBar;

    private static final String[] PRESET_QUESTIONS = {
            "How do I register a new account?",
            "What authentication methods are supported?",
            "How to import transactions from WeChat?",
            "How to set up a savings goal?",
            "What security features are available?",
            "How to edit my user info?",
            "Can I Upload a screenshot to import a transaction?",
            "Can I import my transaction history?",
            "How to set up budget?",
            "Any feature on account management?"
    };

    public HelpDialog(JFrame parent) {
        super(parent, "AI Help Assistant", true);
        this.assistant = new AIAssistant();
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.add(new JLabel("Ask questions about the app features"));
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel with left-right split
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Left panel - Preset questions
        JPanel presetPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        presetPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Common Questions"),
                new EmptyBorder(10, 10, 10, 10)));

        for (String question : PRESET_QUESTIONS) {
            JButton btn = new JButton(question);
            btn.addActionListener(e -> questionArea.setText(question));
            presetPanel.add(btn);
        }

        // Right panel - Answer and input
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Answer area
        answerArea = new JTextArea(15, 40);
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(BorderFactory.createTitledBorder("Answer"));

        // Question input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        questionArea = new JTextArea(3, 40);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(questionArea), BorderLayout.CENTER);

        JButton askButton = new JButton("Ask");
        askButton.addActionListener(this::handleAskQuestion);
        inputPanel.add(askButton, BorderLayout.EAST);

        // Add components to right panel
        rightPanel.add(new JScrollPane(answerArea), BorderLayout.CENTER);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        // Add left and right panels to content panel
        contentPanel.add(presetPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);

        // Final layout
        add(contentPanel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
    }

    private void handleAskQuestion(ActionEvent e) {
        String question = questionArea.getText().trim();
        if (question.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question",
                    "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        setLoadingState(true);
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return assistant.getHelpResponse(question);
            }

            @Override
            protected void done() {
                try {
                    answerArea.setText(get());
                } catch (Exception ex) {
                    answerArea.setText("Error getting help: " + ex.getMessage());
                } finally {
                    setLoadingState(false);
                }
            }
        }.execute();
    }

    private void setLoadingState(boolean loading) {
        progressBar.setIndeterminate(loading);
        progressBar.setVisible(loading);
        questionArea.setEnabled(!loading);

        if (loading) {
            answerArea.setText("Thinking... Please wait.");
        }
    }

    public static void showHelpDialog(JFrame parent) {
        HelpDialog dialog = new HelpDialog(parent);
        dialog.setVisible(true);
    }
}
