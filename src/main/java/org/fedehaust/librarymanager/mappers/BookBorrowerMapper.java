package org.fedehaust.librarymanager.mappers;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.entities.BookBorrower;

import java.util.List;
import java.util.stream.Collectors;

public class BookBorrowerMapper {

    public static BookBorrowedResponse bookBorrowerToDto(BookBorrower bookBorrower) {
        return new BookBorrowedResponse(
                bookBorrower.getId(),
                bookBorrower.getBook().getTitle(),
                bookBorrower.getBorrowDate(),
                bookBorrower.isReturned());
    }

    public static List<BookBorrowedResponse> bookBorrowersToDtoList(List<BookBorrower> bookBorrowers) {
        return bookBorrowers
                .stream()
                .map(BookBorrowerMapper::bookBorrowerToDto)
                .collect(Collectors.toList());
    }
}
