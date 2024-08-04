package org.fedehaust.librarymanager.services.interfaces;

import org.fedehaust.librarymanager.entities.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BorrowersService {

    public List<Borrower> findAllBorrowers();

    public Borrower findBorrowerById(Long id);

    public Borrower findBorrowerByEmail(String email);

    public void createBorrower(Borrower borrower);

    public void updateBorrower(Borrower borrower);

    public void deleteBorrower(Long id);

    public Page<Borrower> findPaginated(Pageable pageable);
}
