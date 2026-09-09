/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;

public class Garcia_Problem8{

    public static void main(String[] args) {
        String studentName;
        String subjectCode;
        String subjectTitle;
        int units;
        char section;
        
        Scanner read = new Scanner(System.in);
        studentName = read.nextLine();
        subjectCode = read.nextLine();
        subjectTitle = read.nextLine();
        units = read.nextInt();
        section = read.next().charAt(0);

        System.out.println("Student Name: " + studentName);
        System.out.println("Subject Code: " + subjectCode);
        System.out.println("Subject Title: " + subjectTitle);
        System.out.println("Units: " + units);
        System.out.println("Section: " + section);

        read.close();
    }
}