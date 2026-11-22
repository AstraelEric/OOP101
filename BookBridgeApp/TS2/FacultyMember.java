package com.mycompany.bookbridge;

public class FacultyMember extends Member {
    public FacultyMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaximumLoanDays() {
        return 30;
    }

    @Override
    public int getDailyFine() {
        return 2;
    }

    @Override
    public String getMembershipType() {
        return "FACULTY";
    }
}
