package com.mycompany.bookbridge;

/** Abstract member from the UML. Subclasses supply their borrowing policies. */
public abstract class Member {
    private final String memberId;
    private final String name;

    protected Member(String memberId, String name) {
        if (memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Member ID cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name cannot be blank.");
        }
        this.memberId = memberId.strip();
        this.name = name.strip();
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public abstract int getMaximumLoanDays();

    public abstract int getDailyFine();

    public abstract String getMembershipType();

    @Override
    public String toString() {
        return name + " (" + memberId + ") - " + getMembershipType();
    }
}
