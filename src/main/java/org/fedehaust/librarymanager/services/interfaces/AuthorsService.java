package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;

import java.util.List;

public interface AuthorsService {

    List<AuthorResponse> findAllAuthors();

    AuthorResponse findAuthorById(Long id);

    AuthorResponse createAuthor(AuthorRequest authorRequest);
}
