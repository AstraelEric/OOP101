/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookbridge.dao;

/**
 *
 * @author Enrico Garcia
 */

import com.mycompany.bookbridge.database.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

public class LoanDao {
    
    public void issueLoan(String memberId, String isbn, int days) throws SQLException {
        String sql = "INSERT INTO loans (member_id, isbn, issue_date, due_date, status, late_days) VALUES (?, ?, ?, ?, 'ACTIVE', 0)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            pstmt.setString(2, isbn);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.setString(4, LocalDate.now().plusDays(days).toString());
            pstmt.executeUpdate();
        }
    }

    public void returnLoan(String isbn) throws SQLException {
        String sql = "UPDATE loans SET status = 'RETURNED' WHERE isbn = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
        }
    }
}