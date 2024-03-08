package org.arjunaoverdrive.bookstore.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arjunaoverdrive.bookstore.configuration.cache.properties.AppCacheProperties;
import org.arjunaoverdrive.bookstore.dao.BookRepository;
import org.arjunaoverdrive.bookstore.exception.CannotSaveEntityException;
import org.arjunaoverdrive.bookstore.exception.EntityNotFoundException;
import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.model.Category;
import org.arjunaoverdrive.bookstore.service.BookService;
import org.arjunaoverdrive.bookstore.service.CategoryService;
import org.arjunaoverdrive.bookstore.utils.BeanUtils;
import org.arjunaoverdrive.bookstore.web.dto.BookFilter;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public Book findById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Book with id {0} not found.", id)
                ));
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR,
            key = "#bookFilter.author + ' ' + #bookFilter.title", unless = "#result == null")
    public Book findByTitleAndAuthor(BookFilter bookFilter) {
        Book book = bookRepository.findBooksByAuthorAndTitle(bookFilter.getAuthor(), bookFilter.getTitle())
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                MessageFormat.format("Book with filter parameters {0} not found", bookFilter)
                        )
                );
        return book;
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#p0", unless = "#result == null || #result.isEmpty()")
    public List<Book> findByCategory(String categoryName) {
        Category category = categoryService.findCategoryByName(categoryName);
        return new ArrayList<>(bookRepository.findAllByCategory(category));
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#book.category.name"),
                    @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR,
                            key = "#book.author + ' ' + #book.title")
            })
    public Book createBook(Book book) {
        Category category = book.getCategory();
        category.addBook(book);
        book.setCreatedAt(Instant.now());
        book.setUpdatedAt(Instant.now());
        book = bookRepository.save(book);
        return book;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#existing.category.name",
                             beforeInvocation = true),
                    @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#bookRequest.category.name",
                            beforeInvocation = true),
                    @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR,
                            key = "#existing.author + ' ' + #existing.title", cacheResolver = "customCacheResolver",
                            beforeInvocation = true),
                    @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR,
                            key = "#bookRequest.author + ' ' + #bookRequest.title", cacheResolver = "customCacheResolver",
                            beforeInvocation = true)
            })
    public Book updateBook(Book existing, Book bookRequest) {
        BeanUtils.copyNonNullProperties(bookRequest, existing);
        existing.setUpdatedAt(Instant.now());
        try {
            bookRepository.save(existing);
        } catch (Exception e) {
            throw new CannotSaveEntityException(e.getMessage());
        }

        return existing;
    }

    @Override
    public void deleteBookById(UUID id) {
        Book toDelete = findById(id);
        bookRepository.delete(toDelete);
    }

}
