/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;

public class Garcia_Problem10 {

    public static void main(String[] args) {
        String name;
        String id;
        String position;
        double hours;
        double rate;
        
        Scanner read = new Scanner(System.in);
        name = read.nextLine();
        id = read.nextLine();
        position = read.nextLine();
        hours = read.nextDouble();
        rate = read.nextDouble();

        System.out.println("Employee Name: " + name);
        System.out.println("Employee ID: " + id);
        System.out.println("Position: " + position);
        System.out.println("Hours Worked: " + hours);
        System.out.println("Hourly Rate: " + rate);

        read.close();
    }
}