package org.fedehaust.librarymanager.exceptions;

public class BorrowerNotFoundException extends NotFoundException {

    public BorrowerNotFoundException(Long id) {
        super(String.format("Borrower not found with ID %d", id));
    }
}
