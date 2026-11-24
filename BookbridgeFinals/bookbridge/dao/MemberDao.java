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
import com.mycompany.bookbridge.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDao {
    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (member_id, name, membership_type) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getMembershipType().name());
            pstmt.executeUpdate();
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("membership_type");
                if (type.equals("STUDENT")) list.add(new StudentMember(rs.getString("member_id"), rs.getString("name")));
                else if (type.equals("FACULTY")) list.add(new FacultyMember(rs.getString("member_id"), rs.getString("name")));
                else list.add(new GuestMember(rs.getString("member_id"), rs.getString("name")));
            }
        }
        return list;
    }
}