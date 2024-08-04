package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.mappers.BooksMapper;
import org.fedehaust.librarymanager.mappers.BorrowersMapper;
import org.fedehaust.librarymanager.services.interfaces.BooksService;

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
@RequestMapping("books/")
public class BooksController {

    @Autowired
    private BooksService booksService;

    @GetMapping()
    public ResponseEntity<List<BookResponse>> getAllBorrowers() {
        var books = booksService.findAllBooks();
        return new ResponseEntity<>(BooksMapper.booksToDtoList(books), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable("id") Long id) {
        try {
            var book = booksService.findBookById(id);
            return new ResponseEntity<>(BooksMapper.bookToDto(book), HttpStatus.OK);
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found");
        }
    }
}