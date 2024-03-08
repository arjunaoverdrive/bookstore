package org.arjunaoverdrive.bookstore.dao;

import org.arjunaoverdrive.bookstore.configuration.cache.properties.AppCacheProperties;
import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.model.Category;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategory(Category category);

    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#book.category.getName()",
                    beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR,
                    key = "#book.author + ' ' + #book.title")
    })
    void delete(Book book);

    List<Book> findBooksByAuthorAndTitle(String author, String title);
}
