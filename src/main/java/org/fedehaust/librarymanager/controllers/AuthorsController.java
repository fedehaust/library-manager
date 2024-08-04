package org.fedehaust.librarymanager.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.mappers.AuthorsMapper;
import org.fedehaust.librarymanager.mappers.BooksMapper;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("authors/")
public class AuthorsController {

    @Autowired
    private AuthorsService authorsService;

    @GetMapping()
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        var authors = authorsService.findAllAuthors();
        return new ResponseEntity<>(AuthorsMapper.authorsToDtoList(authors), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable("id") Long id) {
        try {
            var author = authorsService.findAuthorById(id);
            return new ResponseEntity<>(AuthorsMapper.authorToDto(author), HttpStatus.OK);
        } catch (AuthorNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Not Found");
        }
    }
}
