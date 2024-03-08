package org.arjunaoverdrive.bookstore.mapper;

import org.arjunaoverdrive.bookstore.model.Category;
import org.arjunaoverdrive.bookstore.web.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryResponse toCategoryResponse (Category category);
}
