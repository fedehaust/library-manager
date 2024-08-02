package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.UserBook;

import java.util.List;

public interface UserBookRepository {

    void saveUserBook(UserBook userBook);

    List<UserBook> getAllUserBooks();

    static UserBookJdbcRepository openUserBookRepository(String databaseFile){
        return new UserBookJdbcRepository(databaseFile);
    }
}
