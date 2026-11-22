package com.mycompany.bookbridge;

public class StudentMember extends Member {
    public StudentMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaximumLoanDays() {
        return 14;
    }

    @Override
    public int getDailyFine() {
        return 5;
    }

    @Override
    public String getMembershipType() {
        return "STUDENT";
    }
}
