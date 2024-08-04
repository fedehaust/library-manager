package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;

import java.util.ArrayList;
import java.util.List;

public interface AuthorsService {
    List<AuthorResponse> findAllAuthors();

    AuthorResponse findAuthorById(Long id);

    List<AuthorResponse> findAllAuthorsByIds(ArrayList<Long> authorIds);

    AuthorResponse createAuthor(AuthorRequest authorRequest);

    void updateAuthor(AuthorRequest authorRequest);

    void deleteAuthor(Long id);
}
