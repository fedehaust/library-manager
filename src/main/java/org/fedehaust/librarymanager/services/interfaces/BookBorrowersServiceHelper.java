package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Borrower;

public interface BookBorrowersServiceHelper {
    Borrower getBorrower(Long borrowerId);
}
