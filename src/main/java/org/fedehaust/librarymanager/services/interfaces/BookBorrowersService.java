package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;

public interface BookBorrowersService {

    void returnBook(Long id);

    BookBorrowedResponse findBookBorrowerById(Long id);
}
