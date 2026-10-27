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

public class StudentInfoPanel extends JPanel {

    public StudentInfoPanel() {
        setLayout(new GridLayout(1, 2, 20, 0));
        setBackground(new Color(245, 245, 245));
        setMaximumSize(new Dimension(1100, 120));

        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setBackground(new Color(245, 245, 245));
        leftCol.add(new JLabel("Enrollment Status : ENROLLED"));
        leftCol.add(new JLabel("Student # : 202110524"));
        leftCol.add(new JLabel("Name : GARCIA, ENRICO MALABANAN"));
        leftCol.add(new JLabel("Phone : 09606040530   Zip Code : 1870"));
        leftCol.add(new JLabel("Classification : "));

        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setBackground(new Color(245, 245, 245));
        rightCol.add(new JLabel("College : COMPUTER STUDIES   Program : BSCSSE"));
        rightCol.add(new JLabel("Year Level : 2   1st term, SY 26-27"));
        rightCol.add(new JLabel("Address : Block 95 Lot 4 Road 43, Robinson's Homes East, Brgy. San Jose"));

        add(leftCol);
        add(rightCol);
    }
}