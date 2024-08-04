package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.entities.Book;

import org.fedehaust.librarymanager.entities.Borrower;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface BooksService {

    List<BookResponse> findAllBooks();

    BookResponse findBookById(Long id);

    BookResponse createBook(BookRequest book);

    void updateBook(Book book);

    void deleteBook(Long id);

    Long borrowBook(Long bookId, BookBorrowedRequest bookBorrowedRequest) throws InvalidAttributeValueException;
}
