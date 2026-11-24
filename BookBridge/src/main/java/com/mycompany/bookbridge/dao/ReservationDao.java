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

public class ReservationDao {
    
    public void addReservation(String memberId, String isbn) throws SQLException {
        String sql = "INSERT INTO reservations (member_id, isbn, status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();
        }
    }

    public void cancelReservation(int resId) throws SQLException {
        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE res_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resId);
            pstmt.executeUpdate();
        }
    }
}