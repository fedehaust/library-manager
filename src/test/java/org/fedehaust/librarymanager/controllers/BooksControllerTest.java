package org.fedehaust.librarymanager.controllers;

import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.fedehaust.librarymanager.controllers.TestUtils.convertObjectToJsonBytes;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTest {

    @MockBean
    private BooksService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /books - Ok")
    void testGetAllBooks() throws Exception {
        // Arrange
        var mockBook1 = new BookResponse(1L, "isbn1", "title1", "desc1", "auth1");
        var mockBook2 = new BookResponse(2L, "isbn2", "title2", "desc2", "auth2");
        doReturn(Arrays.asList(mockBook1, mockBook2)).when(service).findAllBooks();

        // Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].isbn", is("isbn1")))
                .andExpect(jsonPath("$[0].title", is("title1")))
                .andExpect(jsonPath("$[0].description", is("desc1")))
                .andExpect(jsonPath("$[0].author", is("auth1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].isbn", is("isbn2")))
                .andExpect(jsonPath("$[1].title", is("title2")))
                .andExpect(jsonPath("$[1].description", is("desc2")))
                .andExpect(jsonPath("$[1].author", is("auth2")));
    }


    @Test
    @DisplayName("GET /books/1 - Ok")
    void testGetBookByIdFound() throws Exception {
        // Arrange
        var mockBook1 = new BookResponse(1L, "isbn1", "title1", "desc1", "auth1");
        doReturn(mockBook1).when(service).findBookById(mockBook1.id());

        // Assert
        mockMvc.perform(get("/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isbn", is("isbn1")))
                .andExpect(jsonPath("$.title", is("title1")))
                .andExpect(jsonPath("$.description", is("desc1")))
                .andExpect(jsonPath("$.author", is("auth1")));
    }

    @Test
    @DisplayName("GET /books/1 - Not Found")
    void testGetBookByIdNotFound() throws Exception {
        // Arrange
        doThrow(BookNotFoundException.class).when(service).findBookById(1L);

        // Act & Assert
        mockMvc.perform(get("/books/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /books - Ok")
    void testCreateBook() throws Exception {
        // Arrange
        var bookRequest = new BookRequest("isbn1", "title1", "desc1", new ArrayList<>());
        var mockBook = new BookResponse(2L, "isbn1", "title1", "desc1", "auth1");
        doReturn(mockBook).when(service).createBook(any());

        // Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/books/2"))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.isbn", is("isbn1")))
                .andExpect(jsonPath("$.title", is("title1")))
                .andExpect(jsonPath("$.description", is("desc1")))
                .andExpect(jsonPath("$.author", is("auth1")));
    }

    @Test
    @DisplayName("POST /books - Bad Request")
    void testCreateBookBadRequest() throws Exception {
        // Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).createBook(any());
    }

    @Test
    @DisplayName("POST /books - Conflict")
    void testCreateBookConflict() throws Exception {
        // Arrange
        var bookRequest = new BookRequest("isbn1", "title1", "desc1", new ArrayList<>());
        doThrow(RuntimeException.class).when(service).createBook(bookRequest);

        // Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookRequest)))
                .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("PUT /books/1/borrow - Ok")
    void testBorrowBookBook() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 1L);
        doReturn(1L).when(service).borrowBook(1L, bookBorrowedRequest);

        // Assert
        mockMvc.perform(put("/books/{id}/borrow", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookBorrowedRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/bookBorrowers/1"))
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    @DisplayName("PUT /books/1/borrow - Bad Request")
    void testBorrowBookBadRequest() throws Exception {
        // Assert
        mockMvc.perform(put("/books/{id}/borrow", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).borrowBook(any(), any());
    }

    @Test
    @DisplayName("PUT /books/1/borrow - Book Not Found")
    void testBorrowBookNotFound() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 1L);
        doThrow(BookNotFoundException.class).when(service).borrowBook(any(), any());

        // Assert
        mockMvc.perform(put("/books/{id}/borrow", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookBorrowedRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /books/1/borrow - Borrower Not Found")
    void testBorrowBookBorrowerNotFound() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 1L);
        doThrow(BorrowerNotFoundException.class).when(service).borrowBook(any(), any());

        // Assert
        mockMvc.perform(put("/books/{id}/borrow", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookBorrowedRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /books/1/borrow - Conflict")
    void testBorrowBookConflict() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 1L);
        doThrow(RuntimeException.class).when(service).borrowBook(1L, bookBorrowedRequest);

        // Assert
        mockMvc.perform(put("/books/{id}/borrow", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(bookBorrowedRequest)))
                .andExpect(status().isConflict());
    }
}