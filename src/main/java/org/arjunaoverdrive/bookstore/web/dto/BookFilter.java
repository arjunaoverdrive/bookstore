package org.arjunaoverdrive.bookstore.web.dto;

import lombok.*;
import org.arjunaoverdrive.bookstore.validation.BookFilterValid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@BookFilterValid
public class BookFilter {
    private String title;
    private String author;
}
