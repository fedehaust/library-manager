package org.fedehaust.librarymanager.controllers;

import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
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

import java.util.*;

import static org.fedehaust.librarymanager.controllers.TestUtils.convertObjectToJsonBytes;
import static org.fedehaust.librarymanager.controllers.TestUtils.DATE_TIME_FORMAT;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BorrowersControllerTest {

    @MockBean
    private BorrowersService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /borrowers - Ok")
    void testGetAllBorrowers() throws Exception {
        // Arrange
        var now = new GregorianCalendar(2024, Calendar.FEBRUARY, 11).getTime();
        ArrayList<BookBorrowedResponse> mockBookBorrowedList = new ArrayList<>();
        var bookBorrowed = new BookBorrowedResponse(25L, "book", now, false);
        mockBookBorrowedList.add(bookBorrowed);

        var mockBorrower1 = new BorrowerResponse(1L, "Name1", "email1", new ArrayList<>());
        var mockBorrower2 = new BorrowerResponse(2L, "Name2", "email2", mockBookBorrowedList);
        doReturn(Arrays.asList(mockBorrower1, mockBorrower2)).when(service).findAllBorrowers(true);

        // Assert
        mockMvc.perform(get("/borrowers?loadBooks=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Name1")))
                .andExpect(jsonPath("$[0].email", is("email1")))
                .andExpect(jsonPath("$[0].bookBorrowedList", is(new ArrayList<>())))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Name2")))
                .andExpect(jsonPath("$[1].email", is("email2")))
                .andExpect(jsonPath("$[1].bookBorrowedList[0].id", is(25)))
                .andExpect(jsonPath("$[1].bookBorrowedList[0].title", is("book")))
                .andExpect(jsonPath("$[1].bookBorrowedList[0].borrowDate", is(DATE_TIME_FORMAT.format(now))))
                .andExpect(jsonPath("$[1].bookBorrowedList[0].isReturned", is(false)));
    }

    @Test
    @DisplayName("GET /borrowers/1/books - Ok")
    void testGetAllBorrowedBooks() throws Exception {
        // Arrange
        var now = new GregorianCalendar(2024, Calendar.FEBRUARY, 11).getTime();
        ArrayList<BookBorrowedResponse> mockBookBorrowedList = new ArrayList<>();
        var bookBorrowed = new BookBorrowedResponse(25L, "book", now, false);
        mockBookBorrowedList.add(bookBorrowed);

        doReturn(mockBookBorrowedList).when(service).findBorrowedBooksByBorrower(1L);

        // Assert
        mockMvc.perform(get("/borrowers/{id}/bookBorrowers", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(25)))
                .andExpect(jsonPath("$[0].title", is("book")))
                .andExpect(jsonPath("$[0].borrowDate", is(DATE_TIME_FORMAT.format(now))))
                .andExpect(jsonPath("$[0].isReturned", is(false)));
    }

    @Test
    @DisplayName("GET /borrowers/2/bookBorrowers - Empty")
    void testGetAllBorrowedBooksNotFound() throws Exception {
        // Arrange
        doThrow(BorrowerNotFoundException.class).when(service).findBorrowedBooksByBorrower(1L);

        // Assert
        mockMvc.perform(get("/borrowers/{id}/bookBorrowers", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName("GET /borrowers/1 - Ok")
    void testGetBorrowerByIdFound() throws Exception {
        // Arrange
        var mockBorrower1 = new BorrowerResponse(1L, "Hannibal Lecter", "email", new ArrayList<>());
        doReturn(mockBorrower1).when(service).findBorrowerById(mockBorrower1.id(), false);

        // Assert
        mockMvc.perform(get("/borrowers/{id}?loadBooks=false", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Hannibal Lecter")))
                .andExpect(jsonPath("$.email", is("email")))
                .andExpect(jsonPath("$.bookBorrowedList", is(new ArrayList<>())));
    }

    @Test
    @DisplayName("GET /borrowers/1 - Not Found")
    void testGetBorrowerByIdNotFound() throws Exception {
        // Arrange
        doThrow(BorrowerNotFoundException.class).when(service).findBorrowerById(1L, false);

        // Act & Assert
        mockMvc.perform(get("/borrowers/{id}?loadBooks=false", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /borrowers - Ok")
    void testCreateBorrower() throws Exception {
        // Arrange
        var borrowerRequest = new BorrowerRequest(
                "First Name",
                "Last Name",
                "email",
                Optional.of("notes"));
        var mockBorrower = new BorrowerResponse(
                1L,
                "First Name Last Name",
                "email",
                new ArrayList<>());
        doReturn(mockBorrower).when(service).createBorrower(any(), eq(false));

        // Assert
        mockMvc.perform(post("/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(borrowerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/borrowers/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("First Name Last Name")))
                .andExpect(jsonPath("$.email", is("email")))
                .andExpect(jsonPath("$.bookBorrowedList", is(new ArrayList<>())));
    }

    @Test
    @DisplayName("POST /borrowers - Bad Request")
    void testCreateBorrowerBadRequest() throws Exception {
        // Assert
        mockMvc.perform(post("/borrowers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, never()).createBorrower(any(), eq(false));
    }

    @Test
    @DisplayName("POST /borrowers - Conflict")
    void testCreateBorrowerConflict() throws Exception {
        // Arrange
        var borrowerRequest = new BorrowerRequest(
                "First Name",
                "Last Name",
                "email",
                "notes".describeConstable());
        doThrow(RuntimeException.class).when(service).createBorrower(borrowerRequest, false);

        // Assert
        mockMvc.perform(post("/borrowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(borrowerRequest)))
                .andExpect(status().isConflict());
    }
}