package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("borrowers")
public class BorrowersController {

    @Autowired
    private BorrowersService borrowersService;

    @Operation(summary = "Returns all the borrowers present in the database filling the borrowed books optionally")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping()
    public ResponseEntity<List<BorrowerResponse>> getAllBorrowers(
            @RequestParam(defaultValue = "false") Boolean loadBooks) {
        return new ResponseEntity<>(borrowersService.findAllBorrowers(loadBooks), HttpStatus.OK);
    }

    @Operation(summary = "Returns all the borrowed books by the specified borrower Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping("{id}/books")
    public ResponseEntity<List<BookBorrowedResponse>> getAllBorrowedBooks(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(borrowersService.findBorrowedBooksByBorrower(id), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower Not Found");
        }
    }

    @Operation(summary = "Returns the borrower with the specified Id filling the borrowed books optionally")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Borrower not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<BorrowerResponse> getBorrower(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "false") Boolean loadBooks) {
        try {
            return new ResponseEntity<>(borrowersService.findBorrowerById(id, loadBooks), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower Not Found");
        }
    }

    @Operation(summary = "Creates an borrower and retrieves the inserted object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, Borrower already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping
    public ResponseEntity<BorrowerResponse> createBorrower(@Valid @RequestBody BorrowerRequest borrowerRequest) {
        try {
            return new ResponseEntity<>(
                    borrowersService.createBorrower(borrowerRequest,
                            false), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Borrower already exists");
        }
    }
}
