package org.fedehaust.librarymanager.cli.records;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserBook(
        int userId,
        int bookId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")Date endDate
) {}
