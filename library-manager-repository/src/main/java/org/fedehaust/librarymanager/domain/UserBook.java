package org.fedehaust.librarymanager.domain;

import java.util.Date;

public record UserBook(int userId, int bookId, Date endDate) { }
