package org.fedehaust.librarymanager.config;

import org.fedehaust.librarymanager.entities.Author;
import org.fedehaust.librarymanager.entities.Book;
import org.fedehaust.librarymanager.entities.Borrower;
import org.fedehaust.librarymanager.services.interfaces.BooksService;
import org.fedehaust.librarymanager.services.interfaces.BorrowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInit {
    @Autowired
    private BooksService booksService;

    @Autowired
    private BorrowersService borrowersService;

    @Bean
    public CommandLineRunner initialCreate() {
        return (args) -> {
            var author = new Author(
                    "Robert Martin",
                    """
                            Robert C. “Uncle Bob” Martin has been a software \
                            professional since 1970 and an international \
                            software consultant since 1990.""");
            var book = new Book(
                    "9780132350884",
                    "Clean Code",
                    """
                            Even bad code can function. But if code isn\u2019t clean, \
                            it can bring a development organization to its knees. \
                            Every year, countless hours and significant resources are \
                            lost because of poorly written code. But it doesn\u2019t \
                            have to be that way.""");
            book.addAuthor(author);
            booksService.createBook(book);

            var book1 = new Book(
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
            book.addAuthor(author);
            booksService.createBook(book1);

            var book2 = new Book(
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
            book2.addAuthor(new Author(
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
                             real-world software modeling and development."""));
            booksService.createBook(book2);

            var borrower1 = new Borrower("Federico", "Haustein", "admin@admin.in");
            borrowersService.createBorrower(borrower1);
            var borrower2 = new Borrower("Lionel Andrés", "Messi Cuccittini", "messi@goat.com");
            borrowersService.createBorrower(borrower2);
        };
    }
}
