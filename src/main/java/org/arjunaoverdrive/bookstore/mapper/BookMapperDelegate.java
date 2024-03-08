package org.arjunaoverdrive.bookstore.mapper;

import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.model.Category;
import org.arjunaoverdrive.bookstore.service.CategoryService;
import org.arjunaoverdrive.bookstore.web.dto.UpsertBookRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class BookMapperDelegate implements BookMapper {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Book toBook(UpsertBookRequest request) {
        Book book = new Book();
        book.setAuthor(request.getAuthor());
        book.setDescription(request.getDescription());
        book.setTitle(request.getTitle());
        book.setCategory(getCategory(request.getCategoryName(), book));
        return book;
    }

    @Override
    public Book toBook(UUID id, UpsertBookRequest request) {
        Book book = toBook(request);
        book.setId(id);
        return book;
    }

    private Category getCategory(String categoryName, Book book) {
        Category category;
        if((category = categoryService.findCategoryByName(categoryName)) != null){
            return category;
        }
        return categoryService.createCategory(categoryName, book);
    }
}
