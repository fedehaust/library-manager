package org.fedehaust.librarymanager.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends EntityBase {

    @Column(length = 250, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(length = 50, nullable = false)
    private String isbn;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "books_authors",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<Author>();

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookBorrower> bookBorrowers = new HashSet<BookBorrower>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public Set<BookBorrower> getBookBorrowers() {
        return bookBorrowers;
    }

    public void setBookBorrowers(Set<BookBorrower> bookBorrowers) {
        this.bookBorrowers = bookBorrowers;
    }

    public Book() {
    }

    public Book(String isbn, String title, String description) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
    }

    public Book(String isbn, String title, String description, Set<Author> authors) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.authors = authors;
    }
}