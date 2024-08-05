package org.fedehaust.librarymanager.integration;

import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.fedehaust.librarymanager.config.DbInit.*;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class BorrowersControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BorrowersService borrowersService;

    @Test
    @DisplayName("GET /borrowers - Ok")
    public void testGetAllBorrowers() throws Exception {
        // Act
        ResponseEntity<List> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/borrowers"),
                List.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBodyList(response);
        assertThat(responseBody.size(), equalTo(2));
    }

    @ParameterizedTest
    @CsvSource({"0,0", "1,1", "100,0"})
    @DisplayName("GET /borrowers/0/bookBorrowers - Ok")
    public void testGetAllBorrowedBooks(int id, int expectedSize) throws Exception {
        // Act
        ResponseEntity<List> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/borrowers/%d/bookBorrowers".formatted(id)),
                List.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBodyList(response);
        assertThat(responseBody.size(), equalTo(expectedSize));
    }

    @Test
    @DisplayName("POST /borrowers - Ok")
    public void testPostCreateBorrower() throws Exception {
        // Arrange
        var borrowerRequest = new BorrowerRequest(
                "First Name",
                "Last Name",
                "email",
                Optional.of("notes"));

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/borrowers"),
                borrowerRequest,
                BorrowerResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).toString(), equalTo("[/borrowers/3]"));

        var responseBody = getBody(response);
        assertThat(responseBody.id(), equalTo(3L));

        var insertedBorrower = borrowersService.findBorrowerById(responseBody.id(), false);

        assertThat(
                insertedBorrower.name(),
                equalTo("%s %s".formatted(borrowerRequest.firstName(), borrowerRequest.lastName())));
        assertThat(insertedBorrower.email(), equalTo(borrowerRequest.email()));
        assertThat(insertedBorrower.bookBorrowedList(), equalTo(new ArrayList<>()));
    }

    @Test
    @DisplayName("POST /borrowers - Coflict")
    public void testPostCreateBorrowerConflict() throws Exception {
        // Arrange
        var borrowerRequest = new BorrowerRequest(
                BORROWER_1.getFirstName(),
                BORROWER_1.getLastName(),
                BORROWER_1.getEmail(),
                null);

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/borrowers"),
                borrowerRequest,
                BorrowerResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("GET /borrowers/1 - Ok")
    public void testGetBorrowerById(boolean loadBooks) throws Exception {
        //Act
        ResponseEntity<BorrowerResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/borrowers/1?loadBooks=%b".formatted(loadBooks)),
                BorrowerResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBody(response);

        assertThat(responseBody.id(), equalTo(BORROWER_1.getId()));
        assertThat(
                responseBody.name(),
                equalTo("%s %s".formatted(BORROWER_1.getFirstName(), BORROWER_1.getLastName())));
        assertThat(responseBody.email(), equalTo(BORROWER_1.getEmail()));
        if (loadBooks) {
            var bookBorrowedResponse = responseBody.bookBorrowedList().get(0);

            assertThat(bookBorrowedResponse.borrowDate(), equalTo(BOOK_BORROWER_1.getBorrowDate()));
            assertThat(bookBorrowedResponse.isReturned(), equalTo(false));
            assertThat(bookBorrowedResponse.title(), equalTo(BOOK_1.getTitle()));
        } else
            assertThat(responseBody.bookBorrowedList(), equalTo(new ArrayList<>()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, -29, Integer.MAX_VALUE})
    @DisplayName("GET /borrowers/0 - Not Found")
    public void testGetBorrowerByIdNotFound(int id) throws Exception {
        // Act
        ResponseEntity<BorrowerResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/borrowers/%d".formatted(id)),
                BorrowerResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
