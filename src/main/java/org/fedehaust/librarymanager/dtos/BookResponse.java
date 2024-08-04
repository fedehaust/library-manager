package org.fedehaust.librarymanager.dtos;

public record BookResponse(Long id, String isbn, String title, String description, String author) {
}
