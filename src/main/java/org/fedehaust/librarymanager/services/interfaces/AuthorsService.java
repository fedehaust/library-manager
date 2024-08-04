package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Author;

import java.util.ArrayList;
import java.util.List;

public interface AuthorsService {
    List<Author> findAllAuthors();

    Author findAuthorById(Long id);

    List<Author> findAllAuthorsbyIds(ArrayList<Long> authorIds);

    Author createAuthor(Author author);

    void updateAuthor(Author author);

    void deleteAuthor(Long id);
}
