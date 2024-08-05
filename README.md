# Windy Soup

Windy soup is a project used to explore the functionalities of Spring Boot, Swagger UI, H2 (in memory), for this project, is implemented a Library management 

## Features

- Books
    -
    - Create a Book with the corresponding author
    - List all books
    - Get a book
    - Borrow a book (Check that the book and borrower exist, and it is not already borrowed)
- Authors
    - 
    - Create author (unique name)
    - List all books
    - Get an Author
- Borrowers
    -
    - Create a borrower (unique email field)
    - List all borrowers (boolean loadBooks optional parameter)
    - Get a borrower (boolean loadBooks optional parameter)
    - List all borrowed books
- BookBorrowers
    -
    - Return a book
    - Get a bookBorrower


Unit testing (controller layer) and Integration testing (end-to-end) with Junit, Mockito and Hamcrest. (TODO: service layer)

Running the project locally and going to http://localhost:8080/ you will be redirected to http://localhost:8080/swagger-ui/index.html automatically and you will observe the different endpoints documantation as follow:

![Swagger UI doc](https://snipboard.io/mZErfI.jpg)

And you can connet to the H2 Database with the data provided by default as well:

![Login to H2](https://snipboard.io/NPpR78.jpg)

The database has some dummy data that will be recreated for each time you run the project 

![Dummy Data](https://s3.amazonaws.com/i.snag.gy/5ax6DM.jpg)

> [!WARNING]
> Don't use this database as final implementation, it's a database that runs in memory, you will lose your records!!!

You can run this project with IntelliJ/Eclipse/etc. or by the command line at the root

First (clean and create the jar files at the target folder)

```bash
  mvn clean verify
```

Second run the jar file and wait until you have the message
    
**Started LibraryManagerApplication in 7.114 seconds (process running for 7.62)**

```bash
java -jar .\target\library-manager-0.0.1-SNAPSHOT.jar
```

## 🔗 Links
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-grey?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Swagger](https://img.shields.io/badge/Swagger%20UI-green?style=for-the-badge&logo=swagger)](https://swagger.io/tools/swagger-ui/)
[![H2 Database](https://img.shields.io/badge/H2%20Database-blue)](https://swagger.io/tools/swagger-ui/)

My LinkedIn

[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/fedehaust/)

