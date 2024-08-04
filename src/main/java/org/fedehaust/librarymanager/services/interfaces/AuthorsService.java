package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Author;

import java.util.ArrayList;
import java.util.List;

public interface AuthorsService {
    public List<Author> findAllAuthors();

    public Author findAuthorById(Long id);

    public List<Author> findAllAuthorsbyIds(ArrayList<Long> authorIds);

    public Author createAuthor(Author author);

    public void updateAuthor(Author author);

    public void deleteAuthor(Long id);
}
