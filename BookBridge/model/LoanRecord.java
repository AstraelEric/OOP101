package com.mycompany.bookbridge.model;

import java.time.LocalDate;
import java.util.Objects;

/** A numbered loan associated with one member and one book. */
public class LoanRecord implements Renewable {
    private static int nextLoanNumber = 1;

    private final int loanNumber;
    private final Member member;
    private final LibraryBook book;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private final int lateDays; // Kept for fine calculation
    private int requestedDays;
    private LoanStatus status;

    public LoanRecord(Member member, LibraryBook book, int requestedDays, int lateDays) {
        this.member = Objects.requireNonNull(member, "Member is required.");
        this.book = Objects.requireNonNull(book, "Book is required.");
        if (requestedDays < 1 || requestedDays > 30 || requestedDays > member.getMaximumLoanDays()) {
            throw new IllegalArgumentException("Loan days exceed the allowed range.");
        }
        if (lateDays < 0) {
            throw new IllegalArgumentException("Late days cannot be negative.");
        }

        book.markBorrowed();
        this.loanNumber = nextLoanNumber++;
        this.requestedDays = requestedDays;
        this.lateDays = lateDays;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(requestedDays);
        this.status = LoanStatus.ACTIVE;
    }

    public int getLoanNumber() { return loanNumber; }
    public Member getMember() { return member; }
    public LibraryBook getBook() { return book; }
    public int getRequestedDays() { return requestedDays; }
    public int getLateDays() { return lateDays; }
    public LocalDate getDueDate() { return dueDate; }
    public LoanStatus getStatus() { return status; }

    public long getEstimatedFine() {
        return (long) lateDays * member.getDailyFine();
    }

    public void markReturned() {
        if (status != LoanStatus.RETURNED) {
            this.status = LoanStatus.RETURNED;
            book.markReturned();
        }
    }

    @Override
    public boolean isRenewable() {
        return status == LoanStatus.ACTIVE && lateDays == 0
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
        int remainingDays = member.getMaximumLoanDays() - requestedDays;
        if (additionalDays > remainingDays) {
            throw new IllegalArgumentException("Renewal would exceed the limit.");
        }
        requestedDays += additionalDays;
    }
}