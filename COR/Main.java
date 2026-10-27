/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.cor;

/**
 *
 * @author Enrico Garcia
 */
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 11));
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 11));

        SwingUtilities.invokeLater(() -> {
            new CORFrame().setVisible(true);
        });
    }
}
