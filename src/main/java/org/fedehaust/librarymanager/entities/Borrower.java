package org.fedehaust.librarymanager.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "borrowers", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Borrower extends EntityBase {

    @Column(length = 100, nullable = false)
    private String first_name;

    @Column(length = 100, nullable = false)
    private String last_name;

    @Column(length = 250, nullable = false, unique = true)
    private String email;

    @Column(length = 500)
    private String notes;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Borrower() {
    }

    public Borrower(String firstName, String lastName, String email) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
    }
}
