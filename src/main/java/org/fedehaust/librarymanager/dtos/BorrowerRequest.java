package org.fedehaust.librarymanager.dtos;

import jakarta.annotation.Nullable;

import java.util.Optional;

public record BorrowerRequest(String firstName, String lastName, String email,@Nullable Optional<String> notes) {
}
