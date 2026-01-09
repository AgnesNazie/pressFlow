package se.lexicon.pressflow.dto;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String token,
        String type,
        String username,
        String name,
        String email,
        String[] roles
) {
}
