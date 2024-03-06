package org.arjunaoverdrive.bookstore.service;

import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.web.dto.BookFilter;

import java.util.List;
import java.util.UUID;

public interface BookService  {
    Book findByTitleAndAuthor(BookFilter bookFilter);

    List<Book> findByCategory(String categoryName);

    Book createBook(Book book);

    Book updateBook(Book existing, Book bookRequest);

    void deleteBookById(UUID id);

    Book findById(UUID id);
}
