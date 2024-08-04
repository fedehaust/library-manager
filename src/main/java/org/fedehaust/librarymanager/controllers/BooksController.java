package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.mappers.BooksMapper;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;
import org.fedehaust.librarymanager.services.interfaces.BooksService;

import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("books/")
public class BooksController {

    @Autowired
    private BooksService booksService;

    @Autowired
    private AuthorsService authorsService;

    @Autowired
    private BorrowersService borrowersService;

    @GetMapping()
    public ResponseEntity<List<BookResponse>> getAllBorrowers() {
        var books = booksService.findAllBooks();
        return new ResponseEntity<>(BooksMapper.booksToDtoList(books), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Long id) {
        try {
            var book = booksService.findBookById(id);
            return new ResponseEntity<>(BooksMapper.bookToDto(book), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict, Book not created",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        try {
            var authorIds = bookRequest.authorIds();
            var authorIdsSize = authorIds.size();
            var authors = authorsService.findAllAuthorsbyIds(authorIds);
            if (authorIdsSize <= 0 || authors.size() != authorIdsSize)
                throw new AuthorNotFoundException();

            var book = booksService.createBook(BooksMapper.dtoToEntity(bookRequest, new HashSet<>(authors)));
            return new ResponseEntity<>(BooksMapper.bookToDto(book), HttpStatus.CREATED);
        } catch (AuthorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author/s do not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book not created");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict, Book not created",
                    content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content)})
    @PutMapping("/{id}/borrow/{borrower_id}")
    public ResponseEntity<Long> createBook(@PathVariable("id") Long id, @PathVariable("borrower_id") Long borrowerId) {
        try {
            var borrower = borrowersService.findBorrowerById(borrowerId);
            var book = booksService.findBookById(id);

            Long borrowBook = booksService.borrowBook(book,borrower);
            return new ResponseEntity<>(borrowBook, HttpStatus.CREATED);
        } catch (BorrowerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower does not exist");
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book does not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Borrow not created");
        }
    }
}
