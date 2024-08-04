package org.fedehaust.librarymanager.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author extends EntityBase {

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(length = 1000, nullable = false)
    private String description;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            mappedBy = "authors")
    private final Set<Book> books = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public Author(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Author() {
    }
}
