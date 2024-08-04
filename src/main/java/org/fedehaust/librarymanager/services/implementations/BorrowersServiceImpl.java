package org.fedehaust.librarymanager.services.implementations;

import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.exceptions.BorrowerNotFoundException;
import org.fedehaust.librarymanager.repositories.BorrowersRepository;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
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
public class BorrowersServiceImpl implements BorrowersService {

    private final BorrowersRepository borrowersRepository;

    public BorrowersServiceImpl(BorrowersRepository borrowersRepository) {
        this.borrowersRepository = borrowersRepository;
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
    public void createBorrower(Borrower borrower) {
        borrowersRepository.save(borrower);
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
    public Page<Borrower> findPaginated(Pageable pageable) {
        List<Borrower> allBorrowers = findAllBorrowers();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Borrower> list;

        if (allBorrowers.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allBorrowers.size());
            list = allBorrowers.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), allBorrowers.size());
    }
}
