package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.entities.Borrower;

import java.util.List;

public interface BorrowersService {

    List<Borrower> findAllBorrowers();

    Borrower findBorrowerById(Long id);

    Borrower findBorrowerByEmail(String email);

    Borrower createBorrower(Borrower borrower);

    void updateBorrower(Borrower borrower);

    void deleteBorrower(Long id);

    List<BookBorrower> findBorrowedBooksByBorrower(Long id);
}
