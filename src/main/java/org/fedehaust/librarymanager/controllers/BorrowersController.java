package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.mappers.BorrowersMapper;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("borrowers/")
public class BorrowersController {

    @Autowired
    private BorrowersService borrowersService;

    @GetMapping()
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers() {
        var borrowers = borrowersService.findAllBorrowers();
        return new ResponseEntity<>(BorrowersMapper.borrowersToDtoList(borrowers), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrower not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrower(@PathVariable("id") Long id) {
        try {
            var borrower = borrowersService.findBorrowerById(id);
            return new ResponseEntity<>(BorrowersMapper.borrowerToDto(borrower), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower Not Found");
        }
    }
}
