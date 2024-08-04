package org.fedehaust.librarymanager.dtos;

import java.util.ArrayList;

public record BookRequest(String isbn, String title, String description, ArrayList<Long> authorIds) {
}
