package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.entities.Book;
import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.repositories.BooksRepository;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final BookBorrowersRepository bookBorrowersRepository;

    public BooksServiceImpl(
            BooksRepository booksRepository,
            BookBorrowersRepository bookBorrowersRepository) {
        this.booksRepository = booksRepository;
        this.bookBorrowersRepository = bookBorrowersRepository;
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
    public Book createBook(Book book) {
        return booksRepository.save(book);
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
    public Long borrowBook(Book book, Borrower borrower) {
        if(bookBorrowersRepository.getByBookId(book.getId()).isPresent())
            throw  new RuntimeException();

        var bookBorrower = new BookBorrower(book, borrower);
        return bookBorrowersRepository.save(bookBorrower).getId();
    }
}
