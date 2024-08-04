package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Book;

import org.fedehaust.librarymanager.entities.Borrower;

import java.util.List;

public interface BooksService {

    public List<Book> findAllBooks();

    public Book findBookById(Long id);

    public Book createBook(Book book);

    public void updateBook(Book book);

    public void deleteBook(Long id);

    Long borrowBook(Book book, Borrower borrower);
}
