package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.exceptions.BookBorrowerNotFoundException;
import org.fedehaust.librarymanager.mappers.BookBorrowerMapper;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersService;
import org.springframework.stereotype.Service;

@Service
public class BookBorrowersServiceImpl implements BookBorrowersService {

    private final BookBorrowersRepository bookBorrowersRepository;

    public BookBorrowersServiceImpl(BookBorrowersRepository bookBorrowersRepository) {
        this.bookBorrowersRepository = bookBorrowersRepository;
    }

    @Override
    public void returnBook(Long id) {
        var bookBorrower = bookBorrowersRepository.findById(id)
                .orElseThrow(() -> new BookBorrowerNotFoundException(id));

        bookBorrowersRepository.returnBook(id);
    }

    @Override
    public BookBorrowedResponse findBookBorrowerById(Long id) {
        return BookBorrowerMapper.bookBorrowerToDto(
                bookBorrowersRepository.findById(id)
                        .orElseThrow(() -> new BookBorrowerNotFoundException(id)));
    }
}
