package org.fedehaust.librarymanager.dtos;

import java.util.ArrayList;
import java.util.Optional;

public record BookRequest(String isbn, String title, String description, ArrayList<Long> authorIds) {
}
