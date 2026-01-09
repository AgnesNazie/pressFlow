package se.lexicon.pressflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.pressflow.dto.AuthRequestDto;
import se.lexicon.pressflow.dto.AuthResponseDto;
import se.lexicon.pressflow.dto.PersonRegistrationDto;
import se.lexicon.pressflow.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @SecurityRequirements
    @Operation(summary = "Login to get JWT token",
            description = "Authenticate user credentials and return JWT token")
    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto request) {
        return authService.login(request);
    }

    @Operation(summary = "Logout and revoke token",
            description = "Invalidates the current JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully logged out")
    @ApiResponse(responseCode = "400", description = "Invalid token or no token provided")
    @PostMapping("/logout")
    public void logout(@NotNull(message = "Authorization header cannot be null") @RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
    }

    // Add the registration endpoint here
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody PersonRegistrationDto dto) {
        authService.register(dto);
        return ResponseEntity.ok("User registered successfully");
    }
}
