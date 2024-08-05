package org.fedehaust.librarymanager.integration;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.services.interfaces.AuthorsService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.fedehaust.librarymanager.config.DbInit.AUTHOR_1;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.getBodyList;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.createRequestUrl;
import static org.fedehaust.librarymanager.integration.IntegrationUtils.getBody;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class AuthorsControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthorsService authorsService;

    @Test
    @DisplayName("GET /authors - Ok")
    public void testGetAllAuthors() throws Exception {
        // Act
        ResponseEntity<List> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/authors"),
                List.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBodyList(response);
        assertThat(responseBody.size(), equalTo(2));
    }

    @Test
    @DisplayName("POST /authors - Ok")
    public void testPostCreateAuthor() throws Exception {
        // Arrange
        var authorRequest = new AuthorRequest("Author Name", "Some description!");

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/authors"),
                authorRequest,
                AuthorResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).toString(), equalTo("[/authors/3]"));

        var responseBody = getBody(response);
        assertThat(responseBody.id(), equalTo(3L));

        var insertedAuthor = authorsService.findAuthorById(responseBody.id());

        assertThat(insertedAuthor.name(), equalTo(authorRequest.name()));
        assertThat(insertedAuthor.description(), equalTo(authorRequest.description()));
    }

    @Test
    @DisplayName("POST /authors - Coflict")
    public void testPostCreateAuthorConflict() throws Exception {
        // Arrange
        var authorRequest = new AuthorRequest(AUTHOR_1.getName(), AUTHOR_1.getDescription());

        // Act
        var response = this.restTemplate.postForEntity(
                createRequestUrl(port, "/authors"),
                authorRequest,
                AuthorResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    }

    @Test
    @DisplayName("GET /authors/1 - Ok")
    public void testGetAuthorById() throws Exception {
        //Act
        ResponseEntity<AuthorResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/authors/1"),
                AuthorResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        var responseBody = getBody(response);

        assertThat(responseBody.id(), equalTo(AUTHOR_1.getId()));
        assertThat(responseBody.name(), equalTo(AUTHOR_1.getName()));
        assertThat(responseBody.description(), equalTo(AUTHOR_1.getDescription()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, -29, Integer.MAX_VALUE})
    @DisplayName("GET /authors/0 - Not Found")
    public void testGetAuthorByIdNotFound(int id) throws Exception {
        // Act
        ResponseEntity<AuthorResponse> response = this.restTemplate.getForEntity(
                createRequestUrl(port, "/authors/%d".formatted(id)),
                AuthorResponse.class);

        // Assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
