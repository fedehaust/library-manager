package org.fedehaust.librarymanager.repositories;

import org.fedehaust.librarymanager.entities.BookBorrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookBorrowersRepository extends JpaRepository<BookBorrower, Long> {

    @Query(value = "select count(*)>0 from Books_Borrowers where book_id = :bookId and is_returned = false", nativeQuery = true)
    boolean isBookBorrowed(@Param("bookId") Long bookId);

    @Query(value = "select * from Books_Borrowers where borrower_id = :borrowerId", nativeQuery = true)
    Optional<List<BookBorrower>> getByBorrowerId(@Param("borrowerId") Long borrowerId);
}
