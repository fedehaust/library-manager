package org.fedehaust.librarymanager.mappers;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.entities.Borrower;

import java.util.*;
import java.util.stream.Collectors;

public class BorrowersMapper {
    public static BorrowerResponse borrowerToDto(Borrower borrower, boolean loadBooks) {
        var booksBorrowed = loadBooks ? borrower
                .getBookBorrowers()
                .stream()
                .map(BookBorrowerMapper::bookBorrowerToDto)
                .collect(Collectors.toList()) :
                Collections.<BookBorrowedResponse>emptyList();

        return new BorrowerResponse(
                borrower.getId(),
                String.join(" ", borrower.getFirstName(), borrower.getLastName()),
                borrower.getEmail(),
                booksBorrowed);
    }

    public static List<BorrowerResponse> borrowersToDtoList(List<Borrower> borrowers, boolean loadBooks) {
        return borrowers
                .stream()
                .map(borrower -> borrowerToDto(borrower, loadBooks))
                .collect(Collectors.toList());
    }

    public static Borrower dtoToEntity(BorrowerRequest borrowerRequest) {
        return new Borrower(
                borrowerRequest.firstName(),
                borrowerRequest.lastName(),
                borrowerRequest.email(),
                borrowerRequest.notes().orElse(null));
    }
}
