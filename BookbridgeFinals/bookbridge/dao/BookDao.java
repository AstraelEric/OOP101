package com.mycompany.bookbridge.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Enrico Garcia
 */



import com.mycompany.bookbridge.database.DatabaseConnection;
import com.mycompany.bookbridge.model.BookStatus;
import com.mycompany.bookbridge.model.LibraryBook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public void addBook(LibraryBook book) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getStatus().name());
            pstmt.executeUpdate();
        }
    }

    public List<LibraryBook> getAllBooks() throws SQLException {
        List<LibraryBook> list = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new LibraryBook(rs.getString("isbn"), rs.getString("title"), BookStatus.valueOf(rs.getString("status"))));
            }
        }
        return list;
    }

    public void updateStatus(String isbn, BookStatus status) throws SQLException {
        String sql = "UPDATE books SET status = ? WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();
        }
    }
}