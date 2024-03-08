package org.arjunaoverdrive.bookstore.service;

import org.arjunaoverdrive.bookstore.model.Book;
import org.arjunaoverdrive.bookstore.model.Category;

public interface CategoryService {

    Category createCategory(String name);

    Category createCategory(String name, Book book);

    Category findCategoryByName(String name);
}
