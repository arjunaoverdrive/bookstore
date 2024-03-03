package org.arjunaoverdrive.bookstore.service;

import org.arjunaoverdrive.bookstore.model.Category;

public interface CategoryService {

    Category createCategory(String name);

    Category findCategoryByName(String name);
}
