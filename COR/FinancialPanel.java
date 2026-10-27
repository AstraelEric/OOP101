/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cor;

/**
 *
 * @author Enrico Garcia
 */
import javax.swing.*;
import java.awt.*;

public class FinancialPanel extends JPanel {

    public FinancialPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setMaximumSize(new Dimension(800, 400)); 

        addRow("Tuition Fee (15)", "31,620.00");
        addRow("Miscellaneous Fee", "12,305.00");
        addRow("ID Validation Fee", "80.00");
        addRow("ITE Computer Laboratory Fee (CS0070)", "4,008.00");
        addRow("SAP Fee (IT0087)", "6,461.00");
        addRow("Science Lab Fee (GED0083)", "4,104.00");
        addRow("Specialized Network (CS0016)", "5,264.00");
        addRow("TOTAL ASSESSMENT", "63,842.00");

        add(Box.createRigidArea(new Dimension(0, 15)));

        addRow("Family Disc 2021_1st Sibling (30% of Tuition Fee)", "(9,486.00)");

        add(Box.createRigidArea(new Dimension(0, 15)));

        addRow("NET TOTAL ASSESSMENT", "54,356.00");

        add(Box.createRigidArea(new Dimension(0, 30)));

        addRow("Down Payment (Upon Enrollment) 40%", "21,742.40");
        addRow("Midterm (Oct 05 - Oct 07 2026) 30%", "16,306.80");
        addRow("Final (Nov 26 - Dec 03 2026) 30%", "16,306.80");
    }

    private void addRow(String label, String amount) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(245, 245, 245));
        
        JLabel lblLabel = new JLabel(label);
        JLabel lblAmount = new JLabel(amount);
        
        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblAmount, BorderLayout.EAST);
        
        row.setMaximumSize(new Dimension(800, 20));
        add(row);
    }
}
