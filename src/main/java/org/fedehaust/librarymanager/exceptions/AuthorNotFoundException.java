package org.fedehaust.librarymanager.exceptions;

public class AuthorNotFoundException extends NotFoundException {

    public AuthorNotFoundException(Long id) {
        super(String.format("Author not found with ID %d", id));
    }
}
