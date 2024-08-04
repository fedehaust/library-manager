package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.repositories.BorrowersRepository;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersServiceHelper;
import org.springframework.stereotype.Service;

@Service
public class BookBorrowersServiceHelperImpl implements BookBorrowersServiceHelper {
    private final BorrowersRepository borrowersRepository;

    public BookBorrowersServiceHelperImpl(BorrowersRepository borrowersRepository) {
        this.borrowersRepository = borrowersRepository;
    }

    public Borrower getBorrower(Long borrowerId) {
        return borrowersRepository
                .findById(borrowerId)
                .orElseThrow(() -> new BorrowerNotFoundException(borrowerId));
    }
}
