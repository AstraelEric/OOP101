package com.mycompany.bookbridge.model;

public class GuestMember extends Member {
    public GuestMember(String memberId, String name) {
        super(memberId, name);
    }

    @Override public int getMaximumLoanDays() { return 7; }
    @Override public int getDailyFine() { return 10; }
    
    @Override 
    public MembershipType getMembershipType() { return MembershipType.GUEST; }
}