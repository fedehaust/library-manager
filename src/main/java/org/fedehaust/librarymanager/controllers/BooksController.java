package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BooksService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("books")
public class BooksController {

    @Autowired
    private BooksService booksService;

    @Operation(summary = "Returns all the books present in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping()
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok().body(booksService.findAllBooks());
    }

    @Operation(summary = "Returns the book with the specified Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().body(booksService.findBookById(id));
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found");
        }
    }

    @Operation(summary = "Creates a book and retrieves the inserted object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "409", description = "Conflict, Book not created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        try {
            var book = booksService.createBook(bookRequest);
            return ResponseEntity
                    .created(new URI("/books/%d".formatted(book.id())))
                    .body(book);
        } catch (AuthorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author/s do not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book not created");
        }
    }

    @Operation(summary = """
            Borrows a book with the specified Id for the corresponding borrower \
            if it is available and returns the corresponding Id created""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "409", description = "Conflict, Book not created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping("{id}/borrow")
    public ResponseEntity<Long> borrowBook(
            @PathVariable("id") Long bookId,
            @Valid @RequestBody BookBorrowedRequest bookBorrowedRequest) {
        try {
            var borrowBookId = booksService.borrowBook(bookId, bookBorrowedRequest);
            return ResponseEntity
                    .created(new URI("/bookBorrows/%d".formatted(borrowBookId)))
                    .body(borrowBookId);
        } catch (BorrowerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower does not exist");
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Borrow not created");
        }
    }
}
