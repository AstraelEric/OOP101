package com.mycompany.bookbridge.app;

import com.mycompany.bookbridge.database.DatabaseInitializer;
import javax.swing.SwingUtilities;

public class BookBridgeApp {
    public static void main(String[] args) {
        // 1. Initialize Database
        DatabaseInitializer.initialize();

        // 2. Launch GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}