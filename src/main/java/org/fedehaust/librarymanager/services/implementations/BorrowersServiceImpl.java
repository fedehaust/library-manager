package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.dtos.BookBorrowedResponse;
import org.fedehaust.librarymanager.dtos.BorrowerRequest;
import org.fedehaust.librarymanager.dtos.BorrowerResponse;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.mappers.BookBorrowerMapper;
import org.fedehaust.librarymanager.mappers.BorrowersMapper;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.repositories.BorrowersRepository;
import org.fedehaust.librarymanager.services.interfaces.BookBorrowersServiceHelper;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class BorrowersServiceImpl implements BorrowersService {

    private final BorrowersRepository borrowersRepository;
    private final BookBorrowersRepository bookBorrowersRepository;
    private final BookBorrowersServiceHelper bookBorrowersServiceHelper;

    public BorrowersServiceImpl(
            BorrowersRepository borrowersRepository,
            BookBorrowersRepository bookBorrowersRepository,
            BookBorrowersServiceHelper bookBorrowersServiceHelper) {
        this.borrowersRepository = borrowersRepository;
        this.bookBorrowersRepository = bookBorrowersRepository;
        this.bookBorrowersServiceHelper = bookBorrowersServiceHelper;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<BorrowerResponse> findAllBorrowers(boolean loadBooks) {
        return BorrowersMapper.borrowersToDtoList(borrowersRepository.findAll(), loadBooks);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public BorrowerResponse findBorrowerById(Long id, boolean loadBooks) {
        return BorrowersMapper.borrowerToDto(bookBorrowersServiceHelper.getBorrower(id), loadBooks);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public BorrowerResponse findBorrowerByEmail(String email, boolean loadBooks) {
        return BorrowersMapper.borrowerToDto
                (borrowersRepository.findByEmail(email)
                                .orElseThrow(() -> new BorrowerNotFoundException(email)),
                        loadBooks);
    }

    @Override
    public BorrowerResponse createBorrower(BorrowerRequest borrowerRequest, boolean loadBooks) {
        var borrower = BorrowersMapper.dtoToEntity(borrowerRequest);
        return BorrowersMapper.borrowerToDto(borrowersRepository.save(borrower), loadBooks);
    }

    @Override
    public void updateBorrower(BorrowerRequest borrowerRequest) {
        var borrower = BorrowersMapper.dtoToEntity(borrowerRequest);
        borrowersRepository.save(borrower);
    }

    @Override
    public void deleteBorrower(Long id) {
        var user = borrowersRepository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException(id));

        borrowersRepository.deleteById(user.getId());
    }

    @Override
    public List<BookBorrowedResponse> findBorrowedBooksByBorrower(Long id) {
        return BookBorrowerMapper.bookBorrowersToDtoList(bookBorrowersRepository
                .getByBorrowerId(id)
                .orElse(Collections.emptyList()));
    }
}
