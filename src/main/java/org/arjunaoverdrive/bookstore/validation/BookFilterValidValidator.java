package org.arjunaoverdrive.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.arjunaoverdrive.bookstore.web.dto.BookFilter;

public class BookFilterValidValidator implements ConstraintValidator<BookFilterValid, BookFilter> {
    @Override
    public boolean isValid(BookFilter value, ConstraintValidatorContext context) {
        return value.getTitle() != null && value.getAuthor() != null;
    }
}
