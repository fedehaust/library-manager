package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.entities.Book;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.repositories.BooksRepository;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;

    public BooksServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<Book> findAllBooks() {
        return booksRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Book findBookById(Long id) {
        return booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public void createBook(Book book) {
        booksRepository.save(book);
    }

    @Override
    public void updateBook(Book book) {
        booksRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        var book = booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        booksRepository.deleteById(book.getId());
    }

    @Override
    public Page<Book> findPaginated(Pageable pageable) {
        List<Book> allBooks = findAllBooks();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;

        if (allBooks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allBooks.size());
            list = allBooks.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), allBooks.size());
    }
}
