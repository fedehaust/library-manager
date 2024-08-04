package org.fedehaust.librarymanager.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class EntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    public Long getId()
    {
        return this.id;
    }

    @SuppressWarnings("unused")
    private void setId(final Long id)
    {
        this.id = id;
    }
}
