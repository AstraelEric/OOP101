package com.mycompany.bookbridge;

/** One book object whose availability changes when borrowed or returned. */
public class LibraryBook {
    private final String isbn;
    private final String title;
    private boolean available;

    public LibraryBook(String isbn, String title, boolean available) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Book title cannot be blank.");
        }
        this.isbn = isbn.strip();
        this.title = title.strip();
        this.available = available;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markBorrowed() {
        if (!available) {
            throw new IllegalStateException("This book is already unavailable.");
        }
        available = false;
    }

    public void markReturned() {
        if (available) {
            throw new IllegalStateException("This book is already available.");
        }
        available = true;
    }
}

