package org.fedehaust.librarymanager.cli.services;

import org.fedehaust.librarymanager.cli.records.Book;
import org.fedehaust.librarymanager.cli.records.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.fedehaust.librarymanager.cli.records.UserBook;
import org.fedehaust.librarymanager.domain.Role;
import org.fedehaust.librarymanager.repository.BookRepository;
import org.fedehaust.librarymanager.repository.UserBookRepository;
import org.fedehaust.librarymanager.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DatabaseInitializerService {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    public DatabaseInitializerService(
            BookRepository bookRepository,
            UserRepository userRepository,
            UserBookRepository userBookRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
    }

    public int seedBasicData() {
        try {
            var books = toBooks("seed/books.json");
            storeBooks(books);

            var users = toUsers("seed/users.json");
            storeUsers(users);

            var usersBooks = toUsersBooks("seed/usersBooks.json");
            storeUserBooks(usersBooks);

            return books.size() + users.size() + usersBooks.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeBooks(List<Book> books) {
        for (var bookToInsert : books) {
            var book = new org.fedehaust.librarymanager.domain.Book(
                    bookToInsert.id(),
                    bookToInsert.title(),
                    bookToInsert.description());

            bookRepository.saveBook(book);
        }
    }

    private void storeUsers(List<User> users) {
        for (var userToInsert : users) {
            var user = new org.fedehaust.librarymanager.domain.User(
                    userToInsert.id(),
                    userToInsert.firstName(),
                    userToInsert.lastName(),
                    Role.valueOf(userToInsert.role()));

            userRepository.saveUser(user);
        }
    }

    private void storeUserBooks(List<UserBook> users) {
        for (var userBookToInsert : users) {
            var userBook = new org.fedehaust.librarymanager.domain.UserBook(
                    userBookToInsert.userId(),
                    userBookToInsert.bookId(),
                    userBookToInsert.endDate());

            userBookRepository.saveUserBook(userBook);
        }
    }

    private List<Book> toBooks(String booksPath) throws IOException {
        var typeRef = new TypeReference<List<Book>>() {
        };
        return OBJECT_MAPPER.readValue(new File(booksPath), typeRef);
    }

    private List<User> toUsers(String usersPath) throws IOException {
        var typeRef = new TypeReference<List<User>>() {
        };
        return OBJECT_MAPPER.readValue(new File(usersPath), typeRef);
    }

    private List<UserBook> toUsersBooks(String usersPath) throws IOException {
        var typeRef = new TypeReference<List<UserBook>>() {
        };
        return OBJECT_MAPPER.readValue(new File(usersPath), typeRef);
    }
}
