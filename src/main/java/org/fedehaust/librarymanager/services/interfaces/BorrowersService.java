package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.entities.Borrower;

import java.util.List;

public interface BorrowersService {

    public List<Borrower> findAllBorrowers();

    public Borrower findBorrowerById(Long id);

    public Borrower findBorrowerByEmail(String email);

    public Borrower createBorrower(Borrower borrower);

    public void updateBorrower(Borrower borrower);

    public void deleteBorrower(Long id);

    List<BookBorrower> findBorrowedBooksByBorrower(Long id);
}
