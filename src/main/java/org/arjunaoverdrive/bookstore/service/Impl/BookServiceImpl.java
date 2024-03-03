package org.arjunaoverdrive.bookstore.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arjunaoverdrive.bookstore.configuration.cache.properties.AppCacheProperties;
import org.arjunaoverdrive.bookstore.dao.BookRepository;
import org.arjunaoverdrive.bookstore.dao.BookSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private Book findById(UUID id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Book with id {0} not found.", id )
                ));
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR, key = "#bookFilter.author + #bookFilter.title")
    public Book findByTitleAndAuthor(BookFilter bookFilter) {
        Specification<Book> specs = BookSpecification.withFilter(bookFilter);
        return bookRepository.findOne(specs).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Book with filter parameters {0} not found", bookFilter)));
    }

    @Override
    @Cacheable(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, key = "#categoryName")
    public List<Book> findByCategory(String categoryName) {
        Category category = categoryService.findCategoryByName(categoryName);
        if(category == null){
            return new ArrayList<>();
        }
        return category.getBooks();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR, allEntries = true, beforeInvocation = true)
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
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR, allEntries = true, beforeInvocation = true)
    })
    public Book updateBook(Book book) {
        Book fromDb = findById(book.getId());
        BeanUtils.copyNonNullProperties(book, fromDb);
        fromDb.setUpdatedAt(Instant.now());

        try{
            bookRepository.save(fromDb);
        }catch (Exception e){
            throw new CannotSaveEntityException(e.getMessage());
        }

        return fromDb;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = AppCacheProperties.CacheNames.FILTERED_BY_TITLE_AND_AUTHOR, allEntries = true, beforeInvocation = true)
    })
    public void deleteBookById(UUID id) {
        Book toDelete = findById(id);
        toDelete.getCategory().deleteBook(toDelete);
        bookRepository.delete(toDelete);
    }

}
