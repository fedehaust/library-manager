package org.fedehaust.librarymanager.integration;

import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersService;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fedehaust.librarymanager.config.DbInit.*;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class BooksControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BooksService booksService;

    @Autowired
    private BookBorrowersService bookBorrowersService;

    @Test
    @DisplayName("GET /books - Ok")
    public void testGetAllBooks() throws Exception {
        // Act
        ResponseEntity<List> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/books"),
                List.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBodyList(response);
        assertThat(responseBody.size(), equalTo(3));
    }

    @Test
    @DisplayName("POST /books - Ok")
    public void testPostCreateBook() throws Exception {
        // Arrange
        ArrayList<Long> authorsIds = new ArrayList<>();
        authorsIds.add(1L);
        var bookRequest = new BookRequest("isbn1", "title1", "desc1", authorsIds);

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/books"),
                bookRequest,
                BookResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).toString(), equalTo("[/books/4]"));

        var responseBody = getBody(response);
        assertThat(responseBody.id(), equalTo(4L));

        var insertedBook = booksService.findBookById(responseBody.id());

        assertThat(insertedBook.isbn(), equalTo(bookRequest.isbn()));
        assertThat(insertedBook.title(), equalTo(bookRequest.title()));
        assertThat(insertedBook.description(), equalTo(bookRequest.description()));
        assertThat(insertedBook.author(), equalTo(AUTHOR_1.getName()));
    }

    @Test
    @DisplayName("POST /books - Bad Request")
    public void testPostCreateBookConflict() throws Exception {
        // Arrange
        var bookRequest = new BookRequest(
                BOOK_1.getIsbn(),
                BOOK_1.getTitle(),
                BOOK_1.getDescription(), new ArrayList<>());

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/books"),
                bookRequest,
                BookResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("GET /books/1 - Ok")
    public void testGetBookById() throws Exception {
        //Act
        ResponseEntity<BookResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/books/1"),
                BookResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBody(response);

        assertThat(responseBody.id(), equalTo(BOOK_1.getId()));
        assertThat(responseBody.isbn(), equalTo(BOOK_1.getIsbn()));
        assertThat(responseBody.title(), equalTo(BOOK_1.getTitle()));
        assertThat(responseBody.description(), equalTo(BOOK_1.getDescription()));
        assertThat(responseBody.author(), equalTo(AUTHOR_1.getName()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, -29, Integer.MAX_VALUE})
    @DisplayName("GET /books/1 - Not Found")
    public void testGetBookByIdNotFound(int id) throws Exception {
        // Act
        ResponseEntity<BookResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/books/%d".formatted(id)),
                BookResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("POST /books/2/borrowbooks - Ok")
    public void testPutBorrowBook() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(2L, 1L);
        HttpEntity<BookBorrowedRequest> httpEntity = new HttpEntity<>(bookBorrowedRequest, new HttpHeaders());

        // Act
        var response = this.restTemplate.exchange(
                createRequestUrl(port, "/books/2/borrow"),
                HttpMethod.PUT,
                httpEntity,
                Void.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).toString(), equalTo("[/bookBorrowers/2]"));


        var insertedBookBorrower = bookBorrowersService.findBookBorrowerById(2L);

        assertThat(insertedBookBorrower.title(), equalTo(BOOK_2.getTitle()));
        assertThat(DATE_FORMAT.format(insertedBookBorrower.borrowDate()), equalTo(DATE_FORMAT.format(new Date())));
        assertThat(insertedBookBorrower.isReturned(), equalTo(false));
    }

    @Test
    @DisplayName("POST /books/0/borrow - Book Not Found")
    public void testPutBorrowBookBookNotFound() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(0L, 1L);
        HttpEntity<BookBorrowedRequest> httpEntity = new HttpEntity<>(bookBorrowedRequest, new HttpHeaders());

        // Act
        var response = this.restTemplate.exchange(
                createRequestUrl(port, "/books/0/borrow"),
                HttpMethod.PUT,
                httpEntity,
                Void.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("POST /books/1/borrow - Borrower Not Found")
    public void testPutBorrowBookBorrowerNotFound() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 25L);
        HttpEntity<BookBorrowedRequest> httpEntity = new HttpEntity<>(bookBorrowedRequest, new HttpHeaders());

        // Act
        var response = this.restTemplate.exchange(
                createRequestUrl(port, "/books/1/borrow"),
                HttpMethod.PUT,
                httpEntity,
                Void.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("POST /books/25/borrow - Conflict")
    public void testPutBorrowBookConflict() throws Exception {
        // Arrange
        var bookBorrowedRequest = new BookBorrowedRequest(1L, 1L);
        HttpEntity<BookBorrowedRequest> httpEntity = new HttpEntity<>(bookBorrowedRequest, new HttpHeaders());

        // Act
        var response = this.restTemplate.exchange(
                createRequestUrl(port, "/books/25/borrow"),
                HttpMethod.PUT,
                httpEntity,
                Void.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    }
}
