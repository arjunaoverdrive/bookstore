package org.arjunaoverdrive.bookstore.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private UUID id;
    private String title;
    private String author;
    private Instant createdAt;
    private Instant updatedAt;
    private CategoryResponse category;
}
