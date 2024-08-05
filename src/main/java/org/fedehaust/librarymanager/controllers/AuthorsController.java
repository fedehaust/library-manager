package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorsController {

    @Autowired
    private AuthorsService authorsService;

    @Operation(summary = "Returns all the authors present in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        return ResponseEntity.ok().body(authorsService.findAllAuthors());
    }

    @Operation(summary = "Returns the author with the specified Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping("{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok().body(authorsService.findAuthorById(id));
        } catch (AuthorNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found");
        }
    }

    @Operation(summary = "Creates an author and retrieves the inserted object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, Author already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        try {
            var author =authorsService.createAuthor(authorRequest);
            return ResponseEntity
                    .created(new URI("/authors/%d".formatted(author.id())))
                    .body(author);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Author already exists");
        }
    }
}
