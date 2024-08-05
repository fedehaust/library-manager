package org.fedehaust.librarymanager.config;

import org.fedehaust.librarymanager.entities.Author;
import org.fedehaust.librarymanager.entities.Book;
import org.fedehaust.librarymanager.entities.BookBorrower;
import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.repositories.BookBorrowersRepository;
import org.fedehaust.librarymanager.repositories.BooksRepository;
import org.fedehaust.librarymanager.repositories.BorrowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInit {

    public static final Author AUTHOR_1 = new Author(
            "Robert Martin",
            """
                    Robert C. “Uncle Bob” Martin has been a software \
                    professional since 1970 and an international \
                    software consultant since 1990.""");
    public static final Author AUTHOR_2 = new Author(
            "Eric Evans",
            """
                    Software design thought leader and founder of Domain \
                    Language, Eric Evans, provides a systematic approach \
                    to domain-driven design, presenting an extensive set \
                    of design best practices, experience-based techniques, \
                    and fundamental principles that facilitate the development \
                    of software projects facing complex domains. Intertwining \
                    system design and development practice, this book \
                    incorporates numerous examples based on actual projects \
                    to illustrate the application of domain-driven design to \
                     real-world software modeling and development.""");

    public static final Book BOOK_1 = new Book(
            "9780132350884",
            "Clean Code",
            """
                    Even bad code can function. But if code isn’t clean, \
                    it can bring a development organization to its knees. \
                    Every year, countless hours and significant resources are \
                    lost because of poorly written code. But it does not \
                    have to be that way.""");
    public static final Book BOOK_2 = new Book(
            "9780134494166",
            """
                    Clean Architecture: A Craftsman's Guide to Software \
                    Structure and Design: A Craftsman's Guide to Software \
                    Structure and Design""",
            """
                    Building upon the success of best-sellers The Clean \
                    Coder and Clean Code, legendary software craftsman \
                    Robert C. "Uncle Bob" Martin shows how to bring greater \
                    professionalism and discipline to application architecture \
                    and design.""");
    public static final Book BOOK_3 = new Book(
            "9780321125217",
            "Domain-Driven Design: Tackling Complexity in the Heart of Software",
            """
                    Software design thought leader and founder of Domain \
                    Language, Eric Evans, provides a systematic approach to \
                    domain-driven design, presenting an extensive set of design \
                    best practices, experience-based techniques, and fundamental \
                    principles that facilitate the development of software \
                    projects facing complex domains. Intertwining system design \
                    and development practice, this book incorporates numerous \
                    examples based on actual projects to illustrate the application \
                    of domain-driven design to real-world software modeling \
                    and development.""");

    public static final Borrower BORROWER_1 = new Borrower("Federico", "Haustein", "admin@admin.in");
    public static final Borrower BORROWER_2 = new Borrower("Lionel Andrés", "Messi Cuccittini", "messi@goat.com");

    public static final BookBorrower BOOK_BORROWER_1 = new BookBorrower(BOOK_1,BORROWER_1);

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private BorrowersRepository borrowersRepository;

    @Autowired
    private BookBorrowersRepository bookBorrowersRepository;

    @Bean
    public CommandLineRunner initialCreate() {
        return (args) -> {
            BOOK_1.addAuthor(AUTHOR_1);
            booksRepository.save(BOOK_1);

            BOOK_1.addAuthor(AUTHOR_1);
            booksRepository.save(BOOK_2);

            BOOK_3.addAuthor(AUTHOR_2);
            booksRepository.save(BOOK_3);

            borrowersRepository.save(BORROWER_1);
            borrowersRepository.save(BORROWER_2);

            bookBorrowersRepository.save(BOOK_BORROWER_1);
        };
    }
}
