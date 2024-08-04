package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BooksService {

    public List<Book> findAllBooks();

    public Book findBookById(Long id);

    public void createBook(Book book);

    public void updateBook(Book book);

    public void deleteBook(Long id);

    public Page<Book> findPaginated(Pageable pageable);

}
