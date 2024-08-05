package org.fedehaust.librarymanager.controllers;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.fedehaust.librarymanager.controllers.TestUtils.convertObjectToJsonBytes;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthorsControllerTest {
    @MockBean
    private AuthorsService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /authors - Ok")
    void testGetAuthors() throws Exception {
        // Arrange
        var mockAuthor1 = new AuthorResponse(1L, "Hannibal Lecter", "Some description");
        var mockAuthor2 = new AuthorResponse(2L, "Red John", "Some description.");
        doReturn(Arrays.asList(mockAuthor1, mockAuthor2)).when(service).findAllAuthors();

        // Assert
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Hannibal Lecter")))
                .andExpect(jsonPath("$[0].description", is("Some description")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Red John")))
                .andExpect(jsonPath("$[1].description", is("Some description.")));
    }

    @Test
    @DisplayName("GET /authors/1 - Ok")
    void testGetAuthorByIdFound() throws Exception {
        // Arrange
        var mockAuthor1 = new AuthorResponse(1L, "Hannibal Lecter", "Some description");
        doReturn(mockAuthor1).when(service).findAuthorById(mockAuthor1.id());

        // Assert
        mockMvc.perform(get("/authors/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Hannibal Lecter")))
                .andExpect(jsonPath("$.description", is("Some description")));
    }

    @Test
    @DisplayName("GET /authors/1 - Not Found")
    void testGetAuthorByIdNotFound() throws Exception {
        // Arrange
        doThrow(AuthorNotFoundException.class).when(service).findAuthorById(1L);

        // Act & Assert
        mockMvc.perform(get("/authors/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /authors - Ok")
    void testCreateAuthor() throws Exception {
        // Arrange
        var authorRequest = new AuthorRequest("Author Name", "Some description!");
        var mockAuthor = new AuthorResponse(1L, "Author Name", "Some description!");
        doReturn(mockAuthor).when(service).createAuthor(any());

        // Assert
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(authorRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/authors/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Author Name")))
                .andExpect(jsonPath("$.description", is("Some description!")));
    }

    @Test
    @DisplayName("POST /authors - Bad Request")
    void testCreateAuthorBadRequest() throws Exception {
        // Assert
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).createAuthor(any());
    }

    @Test
    @DisplayName("POST /authors - Conflict")
    void testCreateAuthorConflict() throws Exception {
        // Arrange
        var authorRequest = new AuthorRequest("Author Name", "Some description!");
        doThrow(RuntimeException.class).when(service).createAuthor(authorRequest);

        // Assert
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(authorRequest)))
                .andExpect(status().isConflict());
    }
}