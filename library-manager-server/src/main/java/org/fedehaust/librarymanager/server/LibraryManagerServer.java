package org.fedehaust.librarymanager.server;

import org.fedehaust.librarymanager.repository.BookRepository;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.LogManager;

public class LibraryManagerServer {

    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(LibraryManagerServer.class);
    private static final String BASE_URI = "http://localhost:8080/";

    public static void main(String... args) {
        try {
            String databaseUrl = loadDatabaseFilename();
            LOG.info("Starting HTTP server with database {}", databaseUrl);
            BookRepository courseRepository = BookRepository.openBookRepository(databaseUrl);
            ResourceConfig config = new ResourceConfig().register(new BookResource(courseRepository));

            GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
        } catch (Exception ex) {
            LOG.error("Starting Server", ex);
        }
    }

    private static String loadDatabaseFilename() {
        try (InputStream propertiesStream =
                     LibraryManagerServer.class.getResourceAsStream("/server.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties.getProperty("library-manager.databseUrl");
        } catch (IOException e) {
            throw new IllegalStateException("Could not load database URL");
        }
    }

}