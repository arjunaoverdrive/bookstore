package org.arjunaoverdrive.bookstore.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arjunaoverdrive.bookstore.dao.CategoryRepository;
import org.arjunaoverdrive.bookstore.exception.CannotSaveEntityException;
import org.arjunaoverdrive.bookstore.model.Category;
import org.arjunaoverdrive.bookstore.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;


    @Override
    public Category createCategory(String name) {

        Category category = findCategoryByName(name);
        if(category != null){
            log.info("Category with name {} already exists.", name);
            return category;
        }

        log.debug("Creating category with name {}.", name);
        category = Category.builder().name(name).build();

        try {
            category = repository.save(category);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CannotSaveEntityException(e.getMessage());
        }
        log.info("Saved category {}", category);
        return category;
    }

    @Override
    public Category findCategoryByName(String name) {
        log.debug("Getting category by name {}", name );
        return repository.findByName(name)
                .orElseThrow(null);
    }
}
