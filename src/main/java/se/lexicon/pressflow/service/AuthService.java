package se.lexicon.pressflow.service;

import se.lexicon.pressflow.dto.AuthRequestDto;
import se.lexicon.pressflow.dto.AuthResponseDto;
import se.lexicon.pressflow.dto.PersonRegistrationDto;

public interface AuthService {
    AuthResponseDto login(AuthRequestDto request);

    void logout(String authHeader);
    void register(PersonRegistrationDto dto);
}
