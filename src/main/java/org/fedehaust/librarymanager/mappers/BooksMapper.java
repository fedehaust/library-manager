package org.fedehaust.librarymanager.mappers;

import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.entities.Author;
import org.fedehaust.librarymanager.entities.Book;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BooksMapper {
    public static BookResponse bookToDto(@NotNull Book book) {
        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthors()
                        .stream()
                        .map(Author::getName)
                        .collect(Collectors.joining(", ")));
    }

    public static List<BookResponse> booksToDtoList(List<Book> books) {
        return books
                .stream()
                .map(BooksMapper::bookToDto)
                .collect(Collectors.toList());
    }

    public static Book dtoToEntity(BookRequest bookRequest, Set<Author> authors) {
        return new Book(
                bookRequest.isbn(),
                bookRequest.title(),
                bookRequest.description(),
                authors);
    }
}
