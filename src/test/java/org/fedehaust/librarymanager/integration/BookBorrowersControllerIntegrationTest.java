package org.fedehaust.librarymanager.integration;

import org.apache.hc.client5.http.classic.HttpClient;
import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersService;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.util.Date;

import static org.fedehaust.librarymanager.config.DbInit.*;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class BookBorrowersControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookBorrowersService bookBorrowersService;

    @Test
    @DisplayName("GET /bookBorrowers/1 - Ok")
    public void testGetBookBorrowerById() throws Exception {
        //Act
        ResponseEntity<BookBorrowedResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/bookBorrowers/1"),
                BookBorrowedResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBody(response);

        assertThat(responseBody.id(), equalTo(1L));
        assertThat(DATE_FORMAT.format(responseBody.borrowDate()), equalTo(DATE_FORMAT.format(new Date())));
        assertThat(responseBody.title(), equalTo(BOOK_1.getTitle()));
        assertThat(responseBody.isReturned(), equalTo(false));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, -29, Integer.MAX_VALUE})
    @DisplayName("GET /bookBorrowers/0 - Not Found")
    public void testGetBookBorrowerByIdNotFound(int id) throws Exception {
        // Act
        ResponseEntity<BookBorrowedResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/bookBorrowers/%d".formatted(id)),
                BookBorrowedResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("PATCH /bookBorrowers/1/return - Ok")
    public void testPatchBookBorrowerReturn() throws Exception {
        // Arrange
        HttpEntity<Integer> httpEntity = new HttpEntity<>(1, new HttpHeaders());

        // Act
        customRestTemplateForPatch().patchForObject(
                createRequestUrl(port, "/bookBorrowers/1/return"),
                httpEntity,
                Void.class);

        // Assert
        var bookBorrowerUpdated = bookBorrowersService.findBookBorrowerById(1L);

        assertThat(bookBorrowerUpdated.title(), equalTo(BOOK_1.getTitle()));
        assertThat(DATE_FORMAT.format(bookBorrowerUpdated.borrowDate()), equalTo(DATE_FORMAT.format(new Date())));
        assertThat(bookBorrowerUpdated.isReturned(), equalTo(true));
    }

    @Test
    @DisplayName("PATCH /bookBorrowers/0/return - BookBorrower Not Found")
    public void testPatchBookBorrowerReturnNotFound() throws Exception {
        // Arrange
        HttpEntity<Integer> httpEntity = new HttpEntity<>(2, new HttpHeaders());

        // Act
        assertThrows(HttpClientErrorException.NotFound.class, () ->
                customRestTemplateForPatch().patchForObject(
                        createRequestUrl(port, "/bookBorrowers/2/return"),
                        httpEntity,
                        Void.class));

        // Assert
        var bookBorrowerNotUpdated = bookBorrowersService.findBookBorrowerById(1L);

        assertThat(bookBorrowerNotUpdated.title(), equalTo(BOOK_1.getTitle()));
        assertThat(DATE_FORMAT.format(bookBorrowerNotUpdated.borrowDate()), equalTo(DATE_FORMAT.format(new Date())));
        assertThat(bookBorrowerNotUpdated.isReturned(), equalTo(false));
    }

    // NOTE: As long as TestRestTemplate does not support PATCH
    // https://github.com/spring-projects/spring-framework/issues/23228
    // for testing purposes a new RestTemplate is created to test the PATCH method
    private static RestTemplate customRestTemplateForPatch() {
        RestTemplate restTemplatePatch = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplatePatch.setRequestFactory(requestFactory);
        return restTemplatePatch;
    }
}
