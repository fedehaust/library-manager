package org.fedehaust.librarymanager.dtos;

import java.util.List;

public record BorrowerResponse(Long id, String name, String email, List<BookBorrowedResponse> bookBorrowedList) {
}
