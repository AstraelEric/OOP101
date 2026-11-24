package com.mycompany.bookbridge.model;

/** One book object whose availability changes when borrowed or returned. */
public class LibraryBook {
    private final String isbn;
    private final String title;
    private BookStatus status;

    public LibraryBook(String isbn, String title, BookStatus status) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title cannot be blank.");
        }
        this.isbn = isbn.strip();
        this.title = title.strip();
        this.status = status;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public BookStatus getStatus() { return status; }
    
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void markBorrowed() {
        if (!isAvailable()) {
            throw new IllegalStateException("This book is already checked out.");
        }
        this.status = BookStatus.CHECKED_OUT;
    }

    public void markReturned() {
        if (isAvailable()) {
            throw new IllegalStateException("This book is already available.");
        }
        this.status = BookStatus.AVAILABLE;
    }
}