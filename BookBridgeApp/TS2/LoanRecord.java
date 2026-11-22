package com.mycompany.bookbridge;

import java.util.Objects;

/** A numbered loan associated with one member and one book. */
public class LoanRecord implements Renewable {
    private static int nextLoanNumber = 1;

    private final int loanNumber;
    private final Member member;
    private final LibraryBook book;
    private int requestedDays;
    private final int lateDays;
    private boolean returned;

    public LoanRecord(Member member, LibraryBook book, int requestedDays, int lateDays) {
        this.member = Objects.requireNonNull(member, "Member is required.");
        this.book = Objects.requireNonNull(book, "Book is required.");
        if (requestedDays < 1 || requestedDays > 30
                || requestedDays > member.getMaximumLoanDays()) {
            throw new IllegalArgumentException("Loan days exceed the allowed range.");
        }
        if (lateDays < 0) {
            throw new IllegalArgumentException("Late days cannot be negative.");
        }

        // Validate first, so a failed constructor never borrows the book.
        book.markBorrowed();
        this.loanNumber = nextLoanNumber++;
        this.requestedDays = requestedDays;
        this.lateDays = lateDays;
        this.returned = false;
    }

    public int getLoanNumber() {
        return loanNumber;
    }

    public Member getMember() {
        return member;
    }

    public LibraryBook getBook() {
        return book;
    }

    public int getRequestedDays() {
        return requestedDays;
    }

    public int getLateDays() {
        return lateDays;
    }

    public boolean isReturned() {
        return returned;
    }

    public long getEstimatedFine() {
        return (long) lateDays * member.getDailyFine();
    }

    public void markReturned() {
        // An old loan cannot free a book again after it has been borrowed anew.
        if (!returned) {
            returned = true;
            book.markReturned();
        }
    }

    @Override
    public boolean isRenewable() {
        // Assumption: renew only active, non-overdue loans with days remaining.
        return !returned && lateDays == 0
            && requestedDays < member.getMaximumLoanDays();
    }

    @Override
    public void renew(int additionalDays) {
        if (additionalDays <= 0) {
            throw new IllegalArgumentException("Additional days must be positive.");
        }
        if (!isRenewable()) {
            throw new IllegalStateException("This loan cannot be renewed.");
        }
        // Compare before adding to avoid integer overflow on very large input.
        int remainingDays = member.getMaximumLoanDays() - requestedDays;
        if (additionalDays > remainingDays) {
            throw new IllegalArgumentException(
                "Renewal would exceed the " + member.getMaximumLoanDays()
                + "-day membership limit.");
        }
        requestedDays += additionalDays;
    }
}
