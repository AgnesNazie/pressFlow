package se.lexicon.pressflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import se.lexicon.pressflow.entity.ArticleStatus;

@Builder
public record ArticleUpdateDto(
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
        String title,

        @NotBlank(message = "Content is required")
        String content,
        ArticleStatus status
) {
}
