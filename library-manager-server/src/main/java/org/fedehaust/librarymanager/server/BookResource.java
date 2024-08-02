package org.fedehaust.librarymanager.server;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.fedehaust.librarymanager.domain.Book;
import org.fedehaust.librarymanager.repository.BookRepository;
import org.fedehaust.librarymanager.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.stream.Stream;

@Path("/books")
public class BookResource {
    private static final Logger LOG = LoggerFactory.getLogger(BookResource.class);

    private final BookRepository bookRepository;

    public BookResource(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Stream<Book> getBooks() {
        try {
            return bookRepository
                    .getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::id));
        } catch (RepositoryException e) {
            LOG.error("Could not retrieve books from the database", e);
            throw new NotFoundException();
        }
    }
}
