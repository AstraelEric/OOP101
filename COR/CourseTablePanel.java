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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CourseTablePanel extends JPanel {

    public CourseTablePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        String[] columns = {"Courses", "Title", "Section", "Units", "Days", "Time", "Room"};
        Object[][] data = {
                {"CS0016", "NETWORK AND COMMUNICATIONS 2A", "TN33", "3", "F / M", "07:00-00-08:50:00 / 07:00-00-08:50:00", "ONLINE / F1101"},
                {"CS0070", "OBJECT ORIENTED PROGRAMMING", "TA21", "2", "T", "10:00-00-12:40:00", "ONLINE"},
                {"CS0070L", "OBJECT ORIENTED PROGRAMMING-LAB", "TA21", "1", "M", "10:00-00-12:50:00", "E609"},
                {"GED0059", "MATHEMATICAL METHODS", "M591", "3", "F / T", "19:00-00-20:50:00 / 19:00-00-20:50:00", "ONLINE / ONLINE"},
                {"GED0083", "COLLEGE PHYSICS 2 LECTURE", "TB31", "2", "F", "13:00-00-15:40:00", "ONLINE"},
                {"GED0083L", "COLLEGE PHYSICS 2 LABORATORY", "TB31", "1", "M", "13:00-00-15:50:00", "F1003"},
                {"IT0087", "IT SPECIALIZATION 6 - BUSINESS PROCESS FOR COMPUTING SYSTEM", "TB391", "2", "T", "16:00-00-18:40:00", "ONLINE"},
                {"IT0087L", "IT SPECIALIZATION 6 - BUSINESS PROCESS FOR COMPUTING SYSTEM (LAB )", "TB391", "1", "M", "16:00-00-18:50:00", "F706"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        add(table.getTableHeader(), BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        totalPanel.setBackground(new Color(245, 245, 245));
        totalPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel lblTotal = new JLabel("TOTAL UNITS 15");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 11));
        totalPanel.add(lblTotal);
        
        add(totalPanel, BorderLayout.SOUTH);
    }
}