package org.arjunaoverdrive.bookstore.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertBookRequest {

    @NotNull
    @Size(min = 1, max = 35)
    private String title;
    @NotNull
    @Size(min = 3, max = 35)
    private String author;
    @NotNull
    @Size(min = 3, max = 25)
    private String categoryName;
    private String description;
}
