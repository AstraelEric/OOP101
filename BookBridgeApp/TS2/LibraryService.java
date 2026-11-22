package com.mycompany.bookbridge;

import java.util.Objects;

/** Applies member policies and creates the objects shown in the UML. */
public class LibraryService {

    public RequestResult processRequest(Member member, LibraryBook book,
                                        int requestedDays, int lateDays) {
        Objects.requireNonNull(member, "Member is required.");
        Objects.requireNonNull(book, "Book is required.");
        if (requestedDays < 1 || requestedDays > 30) {
            throw new IllegalArgumentException("Requested loan days must be 1-30.");
        }
        if (lateDays < 0) {
            throw new IllegalArgumentException("Late days cannot be negative.");
        }

        // Capture availability BEFORE an approved loan changes the book's state.
        boolean availableAtRequest = book.isAvailable();
        long estimatedFine = (long) lateDays * member.getDailyFine();
        int maximumDays = member.getMaximumLoanDays();

        // Eligibility comes before availability, including unavailable books.
        if (requestedDays > maximumDays) {
            return new RequestResult(Outcome.DENIED, member, book,
                requestedDays, lateDays, estimatedFine, availableAtRequest,
                "Exceeds the " + maximumDays + "-day membership limit.");
        }

        if (availableAtRequest) {
            LoanRecord loan = new LoanRecord(member, book, requestedDays, lateDays);
            return new RequestResult(Outcome.APPROVED, member, book,
                requestedDays, lateDays, estimatedFine, availableAtRequest,
                "Loan #" + loan.getLoanNumber() + " created.");
        } else {
            Reservation reservation = new Reservation(member, book);
            return new RequestResult(Outcome.RESERVED, member, book,
                requestedDays, lateDays, estimatedFine, availableAtRequest,
                "Reservation created.");
        }
    }
}
