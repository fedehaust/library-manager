package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.repositories.BorrowersRepository;
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

    public BorrowersServiceImpl(
            BorrowersRepository borrowersRepository,
            BookBorrowersRepository bookBorrowersRepository) {
        this.borrowersRepository = borrowersRepository;
        this.bookBorrowersRepository=bookBorrowersRepository;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public List<Borrower> findAllBorrowers() {
        return borrowersRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Borrower findBorrowerById(Long id) {
        return borrowersRepository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException(id));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public Borrower findBorrowerByEmail(String email) {
        return borrowersRepository.findByEmail(email)
                .orElseThrow(() -> new BorrowerNotFoundException(email));
    }

    @Override
    public Borrower createBorrower(Borrower borrower) {
        return borrowersRepository.save(borrower);
    }

    @Override
    public void updateBorrower(Borrower borrower) {
        borrowersRepository.save(borrower);
    }

    @Override
    public void deleteBorrower(Long id) {
        var user = borrowersRepository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException(id));

        borrowersRepository.deleteById(user.getId());
    }

    @Override
    public List<BookBorrower> findBorrowedBooksByBorrower(Long id) {
        return bookBorrowersRepository
                .getByBorrowerId(id)
                .orElse(Collections.<BookBorrower>emptyList());
    }
}
