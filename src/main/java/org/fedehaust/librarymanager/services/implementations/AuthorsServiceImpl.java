package org.fedehaust.librarymanager.services.implementations;

import java.util.Collections;
import java.util.List;

import org.fedehaust.librarymanager.entities.Author;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.fedehaust.librarymanager.services.interfaces.AuthorsService;
import org.fedehaust.librarymanager.repositories.AuthorsRepository;

@Service
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository authorsRepository;

    public AuthorsServiceImpl(AuthorsRepository authorsRepository) {
        this.authorsRepository = authorsRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<Author> findAllAuthors() {
        return authorsRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Author findAuthorById(Long id) {
        return authorsRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public Author createAuthor(Author author) {
        return authorsRepository.save(author);
    }

    @Override
    public void updateAuthor(Author author) {
        authorsRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        var author = authorsRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        authorsRepository.deleteById(author.getId());
    }

    @Override
    public Page<Author> findPaginated(Pageable pageable) {

        var pageSize = pageable.getPageSize();
        var currentPage = pageable.getPageNumber();
        var startItem = currentPage * pageSize;
        List<Author> list;

        if (findAllAuthors().size() < startItem) {
            list = Collections.emptyList();
        } else {
            var toIndex = Math.min(startItem + pageSize, findAllAuthors().size());
            list = findAllAuthors().subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), findAllAuthors().size());

    }

}
