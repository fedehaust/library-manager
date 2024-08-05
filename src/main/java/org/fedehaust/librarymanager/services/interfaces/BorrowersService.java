package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;

import java.util.List;

public interface BorrowersService {

    List<BorrowerResponse> findAllBorrowers(boolean loadBooks);

    BorrowerResponse findBorrowerById(Long id, boolean loadBooks);

    BorrowerResponse createBorrower(BorrowerRequest borrowerRequest, boolean loadBooks);

    List<BookBorrowedResponse> findBorrowedBooksByBorrower(Long id);
}
