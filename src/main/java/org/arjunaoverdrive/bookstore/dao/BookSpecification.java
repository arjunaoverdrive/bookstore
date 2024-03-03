package org.arjunaoverdrive.bookstore.dao;

import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.web.dto.BookFilter;
import org.springframework.data.jpa.domain.Specification;

public interface BookSpecification {
    static Specification<Book> withFilter(BookFilter filter) {
        return Specification.where(byBookTitle(filter.getTitle()))
                .and(byBookAuthor(filter.getAuthor()));
    }

    static Specification<Book> byBookTitle(String title) {
        return (root, query, builder) -> {
            if (title == null || title.isBlank()) {
                return null;
            }
            return builder.equal(root.get("title"), title);
        };
    }

    static Specification<Book> byBookAuthor(String author) {
        return (root, query, builder) -> {
            if (author == null || author.isBlank()) {
                return null;
            }
            return builder.equal(root.get("author"), author);
        };
    }

}
