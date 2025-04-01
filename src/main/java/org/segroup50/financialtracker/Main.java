package org.segroup50.financialtracker;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.segroup50.financialtracker.view.auth.login.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}