package org.fedehaust.librarymanager.exceptions;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String email) {
        super(String.format("Invalid email: %s provided", email));
    }
}
