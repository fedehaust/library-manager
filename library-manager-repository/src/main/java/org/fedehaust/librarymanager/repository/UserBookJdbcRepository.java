package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.UserBook;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserBookJdbcRepository extends JdbcBaseRepository implements UserBookRepository {
    private static final String INSERT_USER_BOOK = """
            MERGE INTO UsersBooks (userId, bookId, endDate)
             VALUES (?, ?, ?)
            """;

    UserBookJdbcRepository(String databaseUrl) {
        super(databaseUrl);
    }

    @Override
    public void saveUserBook(UserBook userBook) {
        executeStatement(INSERT_USER_BOOK, statement -> {
            statement.setInt(1, userBook.userId());
            statement.setInt(2, userBook.bookId());
            statement.setDate(3, new java.sql.Date(userBook.endDate().getTime()));
            statement.execute();
        }, "Failed to insert " + userBook);
    }

    @Override
    public List<UserBook> getAllUserBooks() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UsersBooks");

            List<UserBook> userBooks = new ArrayList<>();
            while (resultSet.next()) {
                UserBook userBook = new UserBook(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getDate(3));
                userBooks.add(userBook);
            }
            return Collections.unmodifiableList(userBooks);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to retrieve userBooks", e);
        }
    }
}
