package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;

import javax.management.InvalidAttributeValueException;
import java.util.List;

public interface BooksService {

    List<BookResponse> findAllBooks();

    BookResponse findBookById(Long id);

    BookResponse createBook(BookRequest bookRequest);

    Long borrowBook(Long bookId, BookBorrowedRequest bookBorrowedRequest) throws InvalidAttributeValueException;
}
