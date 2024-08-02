package org.fedehaust.librarymanager.repository;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcBaseRepository {
    protected DataSource dataSource;

    JdbcBaseRepository(String databaseUrl) {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(databaseUrl);
        this.dataSource = jdbcDataSource;
    }

    protected void executeStatement(
            String sql,
            PreparedStatementConfigurer configurer,
            String errorMsg) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            configurer.configure(statement);
            statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException(errorMsg, e);
        }
    }

    @FunctionalInterface
    interface PreparedStatementConfigurer {
        void configure(PreparedStatement statement) throws SQLException;
    }
}
