/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookbridge.app;

/**
 *
 * @author Enrico Garcia
 */

import com.mycompany.bookbridge.dao.BookDao;
import com.mycompany.bookbridge.dao.MemberDao;
import com.mycompany.bookbridge.dao.ReservationDao;
import com.mycompany.bookbridge.database.DatabaseConnection;
import com.mycompany.bookbridge.model.*;
import com.mycompany.bookbridge.service.LibraryService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MainFrame extends JFrame {
    private final LibraryService service = new LibraryService();
    private final BookDao bookDao = new BookDao();
    private final MemberDao memberDao = new MemberDao();
    private final ReservationDao reservationDao = new ReservationDao();

    // Catalog Tab components
    private JTable catalogTable;
    private DefaultTableModel catalogModel;
    private JTextField isbnField, titleField;

    // Members Tab components
    private JTable memberTable;
    private DefaultTableModel memberModel;
    private JTextField memberIdField, memberNameField;
    private JComboBox<String> memberTypeBox;

    // Circulation Tab components
    private JTextField loanMemberIdField, loanIsbnField, loanDaysField, returnIsbnField;

    // Reservations Tab components
    private JTextField resMemberIdField, resIsbnField;

    // Reports Tab components
    private JTextArea reportArea;
    private JButton generateReportBtn;

    public MainFrame() {
        setTitle("BookBridge - Desktop Library System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Catalog", createCatalogTab());
        tabs.addTab("Members", createMembersTab());
        tabs.addTab("Circulation", createCirculationTab());
        tabs.addTab("Reservations", createReservationsTab());
        tabs.addTab("Reports", createReportsTab());

        add(tabs);
        
        refreshTables();
    }

    // --- 1. CATALOG TAB ---
    private JPanel createCatalogTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        catalogModel = new DefaultTableModel(new String[]{"ISBN", "Title", "Status"}, 0);
        catalogTable = new JTable(catalogModel);
        panel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField(10);
        formPanel.add(isbnField);
        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField(15);
        formPanel.add(titleField);
        JButton addBookBtn = new JButton("Add Book");
        formPanel.add(addBookBtn);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("Refresh Catalog");
        buttonPanel.add(refreshBtn);

        southPanel.add(formPanel);
        southPanel.add(buttonPanel);
        panel.add(southPanel, BorderLayout.SOUTH);

        addBookBtn.addActionListener(e -> {
            try {
                bookDao.addBook(new LibraryBook(isbnField.getText(), titleField.getText(), BookStatus.AVAILABLE));
                JOptionPane.showMessageDialog(this, "Book Added!");
                isbnField.setText(""); titleField.setText("");
                refreshTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshTables());
        return panel;
    }

    // --- 2. MEMBERS TAB ---
    private JPanel createMembersTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        memberModel = new DefaultTableModel(new String[]{"Member ID", "Name", "Type"}, 0);
        memberTable = new JTable(memberModel);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("ID:"));
        memberIdField = new JTextField(8);
        formPanel.add(memberIdField);
        formPanel.add(new JLabel("Name:"));
        memberNameField = new JTextField(12);
        formPanel.add(memberNameField);
        formPanel.add(new JLabel("Type:"));
        memberTypeBox = new JComboBox<>(new String[]{"STUDENT", "FACULTY", "GUEST"});
        formPanel.add(memberTypeBox);
        JButton addMemberBtn = new JButton("Register Member");
        formPanel.add(addMemberBtn);

        panel.add(formPanel, BorderLayout.SOUTH);

        addMemberBtn.addActionListener(e -> {
            try {
                String type = memberTypeBox.getSelectedItem().toString();
                Member m = switch(type) {
                    case "STUDENT" -> new StudentMember(memberIdField.getText(), memberNameField.getText());
                    case "FACULTY" -> new FacultyMember(memberIdField.getText(), memberNameField.getText());
                    default -> new GuestMember(memberIdField.getText(), memberNameField.getText());
                };
                memberDao.addMember(m);
                JOptionPane.showMessageDialog(this, "Member Registered!");
                memberIdField.setText(""); memberNameField.setText("");
                refreshTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    // --- 3. CIRCULATION TAB ---
    private JPanel createCirculationTab() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel issuePanel = new JPanel();
        issuePanel.setBorder(BorderFactory.createTitledBorder("Issue Loan"));
        issuePanel.add(new JLabel("Member ID:"));
        loanMemberIdField = new JTextField(8);
        issuePanel.add(loanMemberIdField);
        issuePanel.add(new JLabel("ISBN:"));
        loanIsbnField = new JTextField(8);
        issuePanel.add(loanIsbnField);
        issuePanel.add(new JLabel("Days (1-30):"));
        loanDaysField = new JTextField(3);
        issuePanel.add(loanDaysField);
        JButton issueBtn = new JButton("Issue Loan");
        issuePanel.add(issueBtn);

        JPanel returnPanel = new JPanel();
        returnPanel.setBorder(BorderFactory.createTitledBorder("Return Book"));
        returnPanel.add(new JLabel("ISBN:"));
        returnIsbnField = new JTextField(10);
        returnPanel.add(returnIsbnField);
        JButton returnBtn = new JButton("Return Book");
        returnPanel.add(returnBtn);

        panel.add(issuePanel);
        panel.add(returnPanel);

        issueBtn.addActionListener(e -> {
            try {
                int days = Integer.parseInt(loanDaysField.getText());
                String msg = service.issueLoan(loanMemberIdField.getText(), loanIsbnField.getText(), days);
                JOptionPane.showMessageDialog(this, msg);
                refreshTables();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Days must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnBtn.addActionListener(e -> {
            try {
                String msg = service.returnBook(returnIsbnField.getText());
                JOptionPane.showMessageDialog(this, msg);
                refreshTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    // --- 4. RESERVATIONS TAB ---
    private JPanel createReservationsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Reserve Book"));
        formPanel.add(new JLabel("Member ID:"));
        resMemberIdField = new JTextField(8);
        formPanel.add(resMemberIdField);
        formPanel.add(new JLabel("ISBN:"));
        resIsbnField = new JTextField(8);
        formPanel.add(resIsbnField);
        JButton reserveBtn = new JButton("Reserve");
        formPanel.add(reserveBtn);

        panel.add(formPanel, BorderLayout.NORTH);

        reserveBtn.addActionListener(e -> {
            try {
                reservationDao.addReservation(resMemberIdField.getText(), resIsbnField.getText());
                JOptionPane.showMessageDialog(this, "Reservation added successfully!");
                resMemberIdField.setText(""); resIsbnField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- 5. REPORTS TAB (SwingWorker Implementation) ---
    private JPanel createReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        generateReportBtn = new JButton("Generate Aggregate Report");
        panel.add(generateReportBtn, BorderLayout.SOUTH);

        generateReportBtn.addActionListener(e -> {
            generateReportBtn.setEnabled(false);
            generateReportBtn.setText("Generating...");
            reportArea.setText("");

            // --- SWINGWORKER: Keeps UI responsive during heavy database reporting ---
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() throws Exception {
                    StringBuilder sb = new StringBuilder();
                    sb.append("=== BOOKBRIDGE CIRCULATION REPORT ===\n\n");

                    String sql = "SELECT " +
                                 "(SELECT COUNT(*) FROM books) AS total_books, " +
                                 "(SELECT COUNT(*) FROM books WHERE status = 'CHECKED_OUT') AS checked_out, " +
                                 "(SELECT COUNT(*) FROM loans WHERE status = 'ACTIVE') AS active_loans, " +
                                 "(SELECT COUNT(*) FROM reservations WHERE status = 'PENDING') AS pending_res";
                    
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql);
                         ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            sb.append("Total Books:       ").append(rs.getInt("total_books")).append("\n");
                            sb.append("Books Checked Out: ").append(rs.getInt("checked_out")).append("\n");
                            sb.append("Active Loans:      ").append(rs.getInt("active_loans")).append("\n");
                            sb.append("Pending Reserves:  ").append(rs.getInt("pending_res")).append("\n");
                        }
                    }
                    
                    // Simulate delay to show the UI remains responsive
                    Thread.sleep(1500);
                    return sb.toString();
                }

                @Override
                protected void done() {
                    try {
                        reportArea.setText(get());
                    } catch (Exception ex) {
                        reportArea.setText("Error generating report: " + ex.getMessage());
                    }
                    generateReportBtn.setEnabled(true);
                    generateReportBtn.setText("Generate Aggregate Report");
                }
            };
            worker.execute();
        });

        return panel;
    }

    // --- BACKGROUND THREADING FOR TABLES ---
    private void refreshTables() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<LibraryBook> books;
            List<Member> members;

            @Override
            protected Void doInBackground() throws Exception {
                books = bookDao.getAllBooks();
                members = memberDao.getAllMembers();
                return null;
            }

            @Override
            protected void done() {
                catalogModel.setRowCount(0);
                for (LibraryBook b : books) {
                    catalogModel.addRow(new Object[]{b.getIsbn(), b.getTitle(), b.getStatus()});
                }

                memberModel.setRowCount(0);
                for (Member m : members) {
                    memberModel.addRow(new Object[]{m.getMemberId(), m.getName(), m.getMembershipType()});
                }
            }
        };
        worker.execute();
    }
}