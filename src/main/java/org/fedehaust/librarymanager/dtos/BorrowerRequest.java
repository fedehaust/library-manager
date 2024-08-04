package org.fedehaust.librarymanager.dtos;

import java.util.Optional;

public record BorrowerRequest(String firstName, String lastName, String email, Optional<String> notes) {
}
