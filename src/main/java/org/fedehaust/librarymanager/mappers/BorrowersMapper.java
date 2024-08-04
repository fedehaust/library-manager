package org.fedehaust.librarymanager.mappers;

import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.entities.Borrower;

import java.util.List;
import java.util.stream.Collectors;

public class BorrowersMapper {
    public static BorrowerResponse borrowerToDto(Borrower borrower) {
        return new BorrowerResponse(
                borrower.getId(),
                String.join(" ", borrower.getFirst_name(), borrower.getLast_name()),
                borrower.getEmail());
    }

    public static List<BorrowerResponse> borrowersToDtoList(List<Borrower> borrowers) {
        return borrowers
                .stream()
                .map(BorrowersMapper::borrowerToDto)
                .collect(Collectors.toList());
    }
}
