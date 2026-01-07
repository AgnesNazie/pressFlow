package se.lexicon.pressflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import se.lexicon.pressflow.entity.Responsibility;

public record TodoAssignmentDto(@NotNull Long todoId,
                                @NotBlank String username,
                                @NotNull Responsibility responsibility) {
}
