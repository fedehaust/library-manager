package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.mappers.AuthorsMapper;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("authors/")
public class AuthorsController {

    @Autowired
    private AuthorsService authorsService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        var authors = authorsService.findAllAuthors();
        return new ResponseEntity<>(AuthorsMapper.authorsToDtoList(authors), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable("id") Long id) {
        try {
            var author = authorsService.findAuthorById(id);
            return new ResponseEntity<>(AuthorsMapper.authorToDto(author), HttpStatus.OK);
        } catch (AuthorNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict, Author already exists", content = @Content)})
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
        try {
            var author = authorsService.createAuthor(AuthorsMapper.dtoToEntity(authorRequest));
            return new ResponseEntity<>(AuthorsMapper.authorToDto(author), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Author already exists");
        }
    }
}
