package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.mappers.BookBorrowerMapper;
import org.fedehaust.librarymanager.mappers.BorrowersMapper;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("borrowers/")
public class BorrowersController {

    @Autowired
    private BorrowersService borrowersService;

    @GetMapping()
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers(@RequestParam Optional<Boolean> loadBooks) {
        var borrowers = borrowersService.findAllBorrowers();
        return new ResponseEntity<>(BorrowersMapper.borrowersToDtoList(borrowers, loadBooks.orElse(false)), HttpStatus.OK);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookBorrowedResponse>> getAllBorrowedBooks(@PathVariable("id") Long id) {
        try {
            var bookBorrowerList = borrowersService.findBorrowedBooksByBorrower(id);
            return new ResponseEntity<>(BookBorrowerMapper.bookBorrowersToDtoList(bookBorrowerList), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower Not Found");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrower not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponse> getBorrower(@PathVariable("id") Long id, @RequestParam Optional<Boolean> loadBooks) {
        try {
            var borrower = borrowersService.findBorrowerById(id);
            return new ResponseEntity<>(BorrowersMapper.borrowerToDto(borrower, loadBooks.orElse(false)), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower Not Found");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict, Borrower already exists",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<BorrowerResponse> createAuthor(@Valid @RequestBody BorrowerRequest borrowerRequest) {
        try {
            var borrower = borrowersService.createBorrower(BorrowersMapper.dtoToEntity(borrowerRequest));
            return new ResponseEntity<>(BorrowersMapper.borrowerToDto(borrower,false), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Borrower already exists");
        }
    }
}
