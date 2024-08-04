package org.fedehaust.librarymanager.dtos;

public record BookBorrowedRequest(Long bookId, Long borrowerId) {
}
