package org.fedehaust.librarymanager.dtos;

import java.util.Date;

public record BookBorrowedResponse(Long id, String title, Date borrowDate, boolean isReturned){}
