package org.fedehaust.librarymanager.mappers;

import org.fedehaust.librarymanager.dtos.AuthorRequest;
import org.fedehaust.librarymanager.dtos.AuthorResponse;
import org.fedehaust.librarymanager.entities.Author;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorsMapper {
    public static AuthorResponse authorToDto(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getDescription());
    }

    public static List<AuthorResponse> authorsToDtoList(List<Author> authors) {
        return authors
                .stream()
                .map(AuthorsMapper::authorToDto)
                .collect(Collectors.toList());
    }

    public static Author dtoToEntity(AuthorRequest authorRequest){
        return new Author(authorRequest.name(),authorRequest.description());
    }
}
