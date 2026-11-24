/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookbridge.database;

/**
 *
 * @author Enrico Garcia
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initialize() {
        String membersTable = """
            CREATE TABLE IF NOT EXISTS members (
                member_id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                membership_type TEXT NOT NULL CHECK (membership_type IN ('STUDENT', 'FACULTY', 'GUEST'))
            );""";
            
        String booksTable = """
            CREATE TABLE IF NOT EXISTS books (
                isbn TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'CHECKED_OUT'))
            );""";
            
        // THIS IS THE FIXED LOANS TABLE WITH late_days
        String loansTable = """
            CREATE TABLE IF NOT EXISTS loans (
                loan_id INTEGER PRIMARY KEY AUTOINCREMENT,
                member_id TEXT NOT NULL,
                isbn TEXT NOT NULL,
                issue_date TEXT NOT NULL,
                due_date TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'RETURNED')),
                late_days INTEGER DEFAULT 0,
                FOREIGN KEY (member_id) REFERENCES members(member_id),
                FOREIGN KEY (isbn) REFERENCES books(isbn)
            );""";
            
        String reservationsTable = """
            CREATE TABLE IF NOT EXISTS reservations (
                reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                member_id TEXT NOT NULL,
                isbn TEXT NOT NULL,
                status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'FULFILLED', 'CANCELLED')),
                FOREIGN KEY (member_id) REFERENCES members(member_id),
                FOREIGN KEY (isbn) REFERENCES books(isbn)
            );""";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(membersTable);
            stmt.execute(booksTable);
            stmt.execute(loansTable);
            stmt.execute(reservationsTable);
            System.out.println("Database initialized and tables verified.");
            
        } catch (SQLException e) {
            System.err.println("DATABASE ERROR: " + e.getMessage());
        }
    }
}