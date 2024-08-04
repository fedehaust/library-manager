package org.fedehaust.librarymanager.exceptions;

public class BookBorrowerNotFoundException extends NotFoundException {

    public BookBorrowerNotFoundException(Long id) {
        super(String.format("BookBorrower not found with ID %d", id));
    }
}
