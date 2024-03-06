package org.arjunaoverdrive.bookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookFilterValidValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BookFilterValid {

    String message() default  "Both author or title must be specified!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
