package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.Book;

import java.util.List;

public interface BookRepository {

    void saveBook(Book book);

    List<Book> getAllBooks();

    static BookRepository openBookRepository(String databaseFile){
        return new BookJdbcRepository(databaseFile);
    }
}
