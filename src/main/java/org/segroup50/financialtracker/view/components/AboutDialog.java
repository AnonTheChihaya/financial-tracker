package org.segroup50.financialtracker.view.components;

import javax.swing.JOptionPane;

public class AboutDialog {
    public static void showAboutDialog() {
        JOptionPane.showMessageDialog(
                null,
                "Finance Tracker v1.0",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
