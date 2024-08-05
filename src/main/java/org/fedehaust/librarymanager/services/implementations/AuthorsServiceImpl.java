package org.fedehaust.librarymanager.services.implementations;

import java.util.List;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.mappers.AuthorsMapper;
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
    public List<AuthorResponse> findAllAuthors() {
        return AuthorsMapper.authorsToDtoList(authorsRepository.findAll());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public AuthorResponse findAuthorById(Long id) {
        return AuthorsMapper.authorToDto(authorsRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id)));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        var author = AuthorsMapper.dtoToEntity(authorRequest);
        return AuthorsMapper.authorToDto(authorsRepository.save(author));
    }
}
