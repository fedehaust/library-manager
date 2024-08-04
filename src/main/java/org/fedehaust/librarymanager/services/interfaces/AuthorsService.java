package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.entities.Author;

import java.util.ArrayList;
import java.util.List;

public interface AuthorsService {
    List<AuthorResponse> findAllAuthors();

    AuthorResponse findAuthorById(Long id);

    List<AuthorResponse> findAllAuthorsbyIds(ArrayList<Long> authorIds);

    AuthorResponse createAuthor(AuthorRequest authorRequest);

    void updateAuthor(Author author);

    void deleteAuthor(Long id);
}
