package org.fedehaust.librarymanager.repository;

import org.fedehaust.librarymanager.domain.Book;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BookJdbcRepository extends JdbcBaseRepository implements BookRepository {
    private static final String INSERT_BOOK = """
            MERGE INTO Books (id, title, description)
             VALUES (?, ?, ?)
            """;

    BookJdbcRepository(String databaseUrl) {
        super(databaseUrl);
    }

    @Override
    public void saveBook(Book book) {
        executeStatement(INSERT_BOOK, statement -> {
            statement.setObject(1, book.id());
            statement.setString(2, book.title());
            statement.setString(3, book.description());
            statement.execute();
        }, "Failed to insert " + book);
    }

    @Override
    public List<Book> getAllBooks() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Books");

            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
                books.add(book);
            }
            return Collections.unmodifiableList(books);
        } catch (SQLException e) {
            throw new RepositoryException("Failed to retrieve books", e);
        }
    }
}
