package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

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
    public Iterable<BorrowerResponse> getAllBorrowers(
            @RequestParam(defaultValue = "false") Boolean loadBooks) {
        return borrowersService.findAllBorrowers(loadBooks);
    }

    @Operation(summary = "Returns all the borrowed books by the specified borrower Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Borrower not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping("{id}/bookBorrowers")
    public Iterable<BookBorrowedResponse> getAllBorrowedBooks(@PathVariable("id") Long id) {
        return borrowersService.findBorrowedBooksByBorrower(id);
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
            return ResponseEntity.ok().body(borrowersService.findBorrowerById(id, loadBooks));
        } catch (BorrowerNotFoundException e) {
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
            var borrower = borrowersService.createBorrower(borrowerRequest, false);
            return ResponseEntity
                    .created(new URI("/borrowers/%d".formatted(borrower.id())))
                    .body(borrower);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Borrower already exists");
        }
    }
}
