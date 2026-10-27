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

public class CORFrame extends JFrame {

    public CORFrame() {
        setTitle("Certificate of Registration");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        mainPanel.add(new StudentInfoPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(new CourseTablePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        mainPanel.add(new FinancialPanel());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
    }
}