package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorsService {
    public List<Author> findAllAuthors();

    public Author findAuthorById(Long id);

    public void createAuthor(Author author);

    public void updateAuthor(Author author);

    public void deleteAuthor(Long id);

    public Page<Author> findPaginated(Pageable pageable);
}
