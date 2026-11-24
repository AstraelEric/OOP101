package com.mycompany.bookbridge.service;

import com.mycompany.bookbridge.dao.BookDao;
import com.mycompany.bookbridge.dao.LoanDao;
import com.mycompany.bookbridge.dao.MemberDao;
import com.mycompany.bookbridge.model.BookStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LibraryService {
    private final BookDao bookDao = new BookDao();
    private final MemberDao memberDao = new MemberDao();
    private final LoanDao loanDao = new LoanDao();

    // SYNCHRONIZED: Fixes the concurrency hazard (Thread-Safe)
    public synchronized String issueLoan(String memberId, String isbn, int days) throws Exception {
        if (days < 1 || days > 30) throw new IllegalArgumentException("Days must be 1-30");
        
        // 1. Check availability in the database
        boolean isAvailable = false;
        String sqlCheck = "SELECT status FROM books WHERE isbn = ?";
        try (Connection conn = com.mycompany.bookbridge.database.DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sqlCheck)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    isAvailable = rs.getString("status").equals("AVAILABLE");
                } else {
                    throw new Exception("Book not found.");
                }
            }
        }
        if (!isAvailable) throw new Exception("Book is already checked out.");

        // 2. Perform updates
        bookDao.updateStatus(isbn, BookStatus.CHECKED_OUT);
        loanDao.issueLoan(memberId, isbn, days);
        
        return "Loan issued successfully!";
    }

    // SYNCHRONIZED: Fixes concurrency hazard
    public synchronized String returnBook(String isbn) throws Exception {
        bookDao.updateStatus(isbn, BookStatus.AVAILABLE);
        loanDao.returnLoan(isbn);
        return "Book returned successfully!";
    }
}