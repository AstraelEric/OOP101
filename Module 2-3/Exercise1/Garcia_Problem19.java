/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;

public class Garcia_Problem19 {

    public static void main(String[] args) {
        int a;
        int b;
        
        Scanner read = new Scanner(System.in);
        a = read.nextInt();
        b = read.nextInt();
        
        System.out.println("AND = " + (a & b));
        System.out.println("OR = " + (a | b));
        System.out.println("XOR = " + (a ^ b));
        System.out.println("NOT a = " + (~a));
        System.out.println("a << 1 = " + (a << 1));
        System.out.println("a >> 1 = " + (a >> 1));
        
        read.close();
    }
}