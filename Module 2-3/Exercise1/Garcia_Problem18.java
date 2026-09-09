/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;

public class Garcia_Problem18 {

    public static void main(String[] args) {
        double grade;
        double attendance;
        boolean eligible;
        
        Scanner read = new Scanner(System.in);
        grade = read.nextDouble();
        attendance = read.nextDouble();
        
        eligible = (grade >= 85) && (attendance >= 80);
        
        System.out.println("Eligible: " + eligible);
        
        read.close();
    }
}