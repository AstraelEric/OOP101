package com.mycompany.bookbridge.model;

import java.util.Objects;

/** Immutable decision data captured when a request is processed. */
public class RequestResult {
    private final Outcome outcome;
    private final Member member;
    private final LibraryBook book;
    private final int requestedDays;
    private final int lateDays;
    private final long estimatedFine;
    private final boolean availableAtRequest;
    private final String detail;

    public RequestResult(Outcome outcome, Member member, LibraryBook book,
                         int requestedDays, int lateDays, long estimatedFine,
                         boolean availableAtRequest, String detail) {
        this.outcome = Objects.requireNonNull(outcome, "Outcome is required.");
        this.member = Objects.requireNonNull(member, "Member is required.");
        this.book = Objects.requireNonNull(book, "Book is required.");
        if (requestedDays < 1 || requestedDays > 30) {
            throw new IllegalArgumentException("Requested days must be 1-30.");
        }
        if (lateDays < 0 || estimatedFine < 0) {
            throw new IllegalArgumentException("Late days and fine cannot be negative.");
        }
        if (detail == null || detail.isBlank()) {
            throw new IllegalArgumentException("Result detail cannot be blank.");
        }
        this.requestedDays = requestedDays;
        this.lateDays = lateDays;
        this.estimatedFine = estimatedFine;
        this.availableAtRequest = availableAtRequest;
        this.detail = detail.strip();
    }

    public Outcome getOutcome() { return outcome; }
    public Member getMember() { return member; }
    public LibraryBook getBook() { return book; }
    public int getRequestedDays() { return requestedDays; }
    public int getLateDays() { return lateDays; }
    public long getEstimatedFine() { return estimatedFine; }
    public boolean isAvailableAtRequest() { return availableAtRequest; }
    public String getDetail() { return detail; }
}