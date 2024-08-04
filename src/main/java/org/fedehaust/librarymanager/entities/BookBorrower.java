package org.fedehaust.librarymanager.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "books_borrowers")
public class BookBorrower extends EntityBase {
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    @Column(name = "borrow_date")
    private Date borrowDate;

    @Column(name = "is_returned")
    private boolean isReturned;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public BookBorrower(Book book, Borrower borrower) {
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = new Date();
    }

    public BookBorrower() {
    }
}
