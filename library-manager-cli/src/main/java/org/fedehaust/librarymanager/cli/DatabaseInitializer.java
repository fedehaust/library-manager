package org.fedehaust.librarymanager.cli;

import org.fedehaust.librarymanager.cli.services.DatabaseInitializerService;
import org.fedehaust.librarymanager.repository.BookRepository;
import org.fedehaust.librarymanager.repository.UserBookRepository;

import org.fedehaust.librarymanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String DATABASE_URL = "jdbc:h2:file:./library.db;AUTO_SERVER=TRUE;INIT=RUNSCRIPT FROM './seed/db_init.sql'";

    public static void main(String... args) {
        LOG.info("DatabaseInitializer starting");
        try {
            seedDatabase();
        } catch (Exception ex) {
            LOG.error("DatabaseInitializer starting", ex);
        }
    }

    private static void seedDatabase() {
        BookRepository bookRepository = BookRepository.openBookRepository(DATABASE_URL);
        UserRepository userRepository = UserRepository.openUserRepository(DATABASE_URL);
        UserBookRepository userBookRepository = UserBookRepository.openUserBookRepository(DATABASE_URL);

        DatabaseInitializerService databaseInitializerService = new DatabaseInitializerService(
                bookRepository,
                userRepository,
                userBookRepository);

        LOG.info("DatabaseInitializer inserted '{}' rows.", databaseInitializerService.seedBasicData());
    }
}
