package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Borrower;

public interface BookBorrowerServiceHelper {
    Borrower getBorrower(Long borrowerId);
}
