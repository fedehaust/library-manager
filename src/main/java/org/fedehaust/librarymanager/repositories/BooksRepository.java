package org.fedehaust.librarymanager.repositories;

import org.fedehaust.librarymanager.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Book, Long> {
}
