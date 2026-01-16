package se.lexicon.pressflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import se.lexicon.pressflow.entity.ArticleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArticleDto(
        Long id,

        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        ArticleStatus status,

        LocalDateTime submittedAt,

        LocalDateTime publishedAt,

        String authorUsername,

        String editorUsername,

        Long todoId,

        int numberOfAttachments,

        List<AttachmentDto> attachments

) {

    public ArticleDto withAttachments(List<AttachmentDto> newAttachments) {
        return new ArticleDto(
                id,
                title,
                content,
                status,
                submittedAt,
                publishedAt,
                authorUsername,
                editorUsername,
                todoId,
                newAttachments != null ? newAttachments.size() : 0,
                newAttachments
        );
    }

    public ArticleDto withStatus(ArticleStatus newStatus) {
        return new ArticleDto(
                id,
                title,
                content,
                newStatus,
                submittedAt,
                publishedAt,
                authorUsername,
                editorUsername,
                todoId,
                numberOfAttachments,
                attachments
        );

        }
    }