package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.User;

import java.util.List;

public interface UserRepository {

    void saveUser(User user);

    List<User> getAllUsers();

    static UserJdbcRepository openUserRepository(String databaseFile){
        return new UserJdbcRepository(databaseFile);
    }
}
