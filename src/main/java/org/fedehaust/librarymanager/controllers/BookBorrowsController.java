package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fedehaust.librarymanager.exceptions.BookBorrowerNotFoundException;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersService;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("bookBorrows")
public class BookBorrowsController {

    @Autowired
    private BookBorrowersService bookBorrowersService;

    @Operation(summary = "Returns a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "304", description = "Not Modified", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PatchMapping("{id}/return")
    public ResponseEntity<Void> borrowBook(@PathVariable("id") Long id) {
        try {
            bookBorrowersService.returnBook(id);
            return ResponseEntity.noContent().build();
        } catch (BookBorrowerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BookBorrower does not exist");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Borrow not created");
        }
    }
}
