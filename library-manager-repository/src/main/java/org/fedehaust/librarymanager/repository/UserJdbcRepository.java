package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.Role;
import org.fedehaust.librarymanager.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserJdbcRepository  extends JdbcBaseRepository implements UserRepository{
    private static final String INSERT_USER = """
            MERGE INTO Users (id, firstName, lastName, role)
             VALUES (?, ?, ?, ?)
            """;

    UserJdbcRepository(String databaseUrl) {
        super(databaseUrl);
    }

    @Override
    public void saveUser(User user) {
        executeStatement(INSERT_USER, statement -> {
            statement.setInt(1, user.id());
            statement.setString(2, user.firstName());
            statement.setString(3, user.lastName());
            statement.setString(4, user.role().name());
            statement.execute();
        }, "Failed to insert " + user);
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        Role.valueOf(resultSet.getString(4)));
                users.add(user);
            }
            return Collections.unmodifiableList(users);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to retrieve users", e);
        }
    }
}
