package org.fedehaust.librarymanager.dtos;

import org.fedehaust.librarymanager.entities.BookBorrower;

import java.util.Date;
import java.util.List;

public record BorrowerResponse(Long id, String name, String email, List<BookBorrowedResponse> bookBorrowedList) {
}
