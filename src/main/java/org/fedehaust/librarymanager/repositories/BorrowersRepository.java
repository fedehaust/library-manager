package org.fedehaust.librarymanager.repositories;

import org.fedehaust.librarymanager.entities.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowersRepository extends JpaRepository<Borrower, Long> {

    Optional<Borrower> findByEmail(String email);
}
