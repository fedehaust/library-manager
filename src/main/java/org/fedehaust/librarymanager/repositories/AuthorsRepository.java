package org.fedehaust.librarymanager.repositories;

import org.fedehaust.librarymanager.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsRepository extends JpaRepository<Author, Long> {
}
