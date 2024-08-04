package org.fedehaust.librarymanager.exceptions;

public class BookNotFoundException extends NotFoundException {

    public BookNotFoundException(Long id) {
        super(String.format("Book not found with ID %d", id));
    }
}
