/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author emgarcia1
 */
import java.util.Scanner;

public class Garcia_Problem20 {

    public static void main(String[] args) {
        double price;
        int quantity;
        double discountPercent;
        double paid;
        double gross, discount, afterDiscount, vat, finalBill, change;
        String status;
        String changeLabel;
        
        Scanner read = new Scanner(System.in);
        System.out.print("Product Price: ");
        price = read.nextDouble();
        System.out.print("Quantity: ");
        quantity = read.nextInt();
        System.out.print("Discount Percentage: ");
        discountPercent = read.nextDouble();
        System.out.print("Amount Paid: ");
        paid = read.nextDouble();
        
        gross = price * quantity;
        discount = gross * discountPercent / 100;
        afterDiscount = gross - discount;
        vat = afterDiscount * 0.12;
        finalBill = afterDiscount + vat;
        change = paid - finalBill;
        status = (paid >= finalBill) ? "SUFFICIENT PAYMENT" : "INSUFFICIENT PAYMENT";
        changeLabel = (paid >= finalBill) ? "Change" : "Difference";
        
        System.out.printf("Gross = %.0f%n", gross);
        System.out.println("Discount = " + discount);
        System.out.println("After Discount = " + afterDiscount);
        System.out.println("VAT = " + vat);
        System.out.println("Final Bill = " + finalBill);
        System.out.printf("%s = %g%n", changeLabel, change);
        System.out.println(status);
        
        read.close();
    }
}