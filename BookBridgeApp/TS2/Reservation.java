package com.mycompany.bookbridge;

import java.util.Objects;

public class Reservation {
    private final Member member;
    private final LibraryBook book;
    private boolean active;

    public Reservation(Member member, LibraryBook book) {
        this.member = Objects.requireNonNull(member, "Member is required.");
        this.book = Objects.requireNonNull(book, "Book is required.");
        this.active = true;
    }

    public Member getMember() {
        return member;
    }

    public LibraryBook getBook() {
        return book;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        // Canceling a reservation does not return someone else's borrowed book.
        active = false;
    }
}
