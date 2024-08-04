package org.fedehaust.librarymanager.dtos;

import java.util.Date;

public record BookBorrowedResponse(String title, Date borrowDate, boolean isReturned){}
