package org.fedehaust.librarymanager.controllers;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.exceptions.BookBorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.fedehaust.librarymanager.controllers.TestUtils.df;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookBorrowsControllerTest {
    @MockBean
    private BookBorrowersService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /bookBorrows/1 - Ok")
    void testGetAuthorByIdFound() throws Exception {
        // Arrange
        var now = new GregorianCalendar(2024, Calendar.FEBRUARY, 11).getTime();
        var mockBookBorrowed = new BookBorrowedResponse(1L,"title",now,false);
        doReturn(mockBookBorrowed).when(service).findBookBorrowerById(mockBookBorrowed.id());

        // Assert
        mockMvc.perform(get("/bookBorrows/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.borrowDate", is(df.format(now))))
                .andExpect(jsonPath("$.isReturned", is(false)));
    }

    @Test
    @DisplayName("GET /bookBorrows/1 - Not Found")
    void testGetAuthorByIdNotFound() throws Exception {
        // Arrange
        doThrow(BookBorrowerNotFoundException.class).when(service).findBookBorrowerById(1L);

        // Act & Assert
        mockMvc.perform(get("/bookBorrows/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /bookBorrows/1/return - Ok")
    void testReturnBook() throws Exception {
        // Arrange
        doNothing().when(service).returnBook(1L);

        // Assert
        mockMvc.perform(patch("/bookBorrows/{id}/return",1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /bookBorrows/1/return - Not Found")
    void testReturnBookNotFound() throws Exception {
        // Arrange
        doThrow(BookBorrowerNotFoundException.class).when(service).returnBook(1L);

        // Assert
        mockMvc.perform(patch("/bookBorrows/{id}/return",1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /bookBorrows/1/return - Not Modified")
    void testReturnBookNotModified() throws Exception {
        // Arrange
        doThrow(RuntimeException.class).when(service).returnBook(1L);

        // Assert
        mockMvc.perform(patch("/bookBorrows/{id}/return",1))
                .andExpect(status().isNotModified());
    }
}