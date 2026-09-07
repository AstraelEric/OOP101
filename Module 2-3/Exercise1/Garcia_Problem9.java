/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;
public class Garcia_Problem9 {
    public static void main(String[] args) {
        String Username;
        String ign;
        int level;
        long exp;
        String rank;
        
        Scanner read = new Scanner(System.in);
        Username = read.nextLine();
        ign = read.nextLine();
        level = read.nextInt();
        exp = read.nextLong();
        read.nextLine();
        rank = read.nextLine();
        
        
        System.out.println("Username: " + Username);
        System.out.println("Character Name: " + ign);
        System.out.println("Level: " + level);
        System.out.println("Experience Points: " + exp);
        System.out.println("Rank: " + rank);
        
        read.close();
    }
}
