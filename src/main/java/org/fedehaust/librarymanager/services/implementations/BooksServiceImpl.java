package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.dtos.BookBorrowedRequest;
import org.fedehaust.librarymanager.dtos.BookRequest;
import org.fedehaust.librarymanager.dtos.BookResponse;
import org.fedehaust.librarymanager.entities.Author;
import org.fedehaust.librarymanager.entities.Book;
import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.exceptions.AuthorNotFoundException;
import org.fedehaust.librarymanager.exceptions.BookNotFoundException;
import org.fedehaust.librarymanager.mappers.BooksMapper;
import org.fedehaust.librarymanager.repositories.AuthorsRepository;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.repositories.BooksRepository;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersServiceHelper;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InvalidAttributeValueException;
import java.util.HashSet;
import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final BookBorrowersRepository bookBorrowersRepository;
    private final AuthorsRepository authorsRepository;
    private final BookBorrowersServiceHelper bookBorrowersServiceHelperImpl;

    public BooksServiceImpl(
            BooksRepository booksRepository,
            BookBorrowersRepository bookBorrowersRepository,
            AuthorsRepository authorsRepository,
            BookBorrowersServiceHelper bookBorrowersServiceHelperImpl) {
        this.booksRepository = booksRepository;
        this.bookBorrowersRepository = bookBorrowersRepository;
        this.authorsRepository = authorsRepository;
        this.bookBorrowersServiceHelperImpl = bookBorrowersServiceHelperImpl;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<BookResponse> findAllBooks() {
        return BooksMapper.booksToDtoList(booksRepository.findAll());
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public BookResponse findBookById(Long id) {
        return BooksMapper.bookToDto(getBook(id));
    }

    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        var bookToSaved = BooksMapper.dtoToEntity(
                bookRequest, validateAndGetAuthors(bookRequest));

        return BooksMapper.bookToDto(booksRepository.save(bookToSaved));
    }

    @Override
    public Long borrowBook(Long bookId, BookBorrowedRequest bookBorrowedRequest) throws InvalidAttributeValueException {
        if (bookId != bookBorrowedRequest.bookId())
            throw new InvalidAttributeValueException();

        var borrower = bookBorrowersServiceHelperImpl.getBorrower(bookBorrowedRequest.borrowerId());
        var book = getBook(bookId);

        if (bookBorrowersRepository.isBookBorrowed(book.getId()))
            throw new RuntimeException();

        return bookBorrowersRepository
                .save(new BookBorrower(book, borrower))
                .getId();
    }

    private @NotNull HashSet<Author> validateAndGetAuthors(BookRequest bookRequest) {
        var authorIds = bookRequest.authorIds();
        var authorIdsSize = authorIds.size();
        var authors = authorsRepository.findAllById(authorIds);

        if (authorIdsSize == 0 || authors.size() != authorIdsSize)
            throw new AuthorNotFoundException();

        return new HashSet<>(authors);
    }

    private Book getBook(Long bookId) {
        return booksRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }
}
