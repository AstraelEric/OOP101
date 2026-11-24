package com.mycompany.bookbridge.model;

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

    public Member getMember() { return member; }
    public LibraryBook getBook() { return book; }
    public boolean isActive() { return active; }

    public void cancel() {
        active = false;
    }
}