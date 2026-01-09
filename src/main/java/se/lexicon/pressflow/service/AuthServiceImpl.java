package se.lexicon.pressflow.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.lexicon.pressflow.dto.AuthRequestDto;
import se.lexicon.pressflow.dto.AuthResponseDto;
import se.lexicon.pressflow.dto.PersonRegistrationDto;
import se.lexicon.pressflow.entity.Person;
import se.lexicon.pressflow.entity.Role;
import se.lexicon.pressflow.entity.User;
import se.lexicon.pressflow.repository.PersonRepository;
import se.lexicon.pressflow.security.JwtTokenUtil;
import se.lexicon.pressflow.security.TokenBlacklistStorage;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService{

    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenBlacklistStorage tokenBlacklistStorage;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenUtil jwtTokenUtil,
                           TokenBlacklistStorage tokenBlacklistStorage,
                           PersonRepository personRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenBlacklistStorage = tokenBlacklistStorage;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtTokenUtil.generateToken(userDetails);

        Person person = personRepository.findByUserUsername(userDetails.getUsername())
                .orElseThrow(() -> new AuthenticationServiceException("Invalid username or password"));

        return AuthResponseDto.builder()
                .token(jwt)
                .type("Bearer")
                .username(userDetails.getUsername())
                .name(person.getName())
                .email(person.getEmail())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .build();
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }

        String token = authHeader.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        try {
            if (tokenBlacklistStorage.isBlacklisted(token)) {
                throw new IllegalArgumentException("Token has already been invalidated");
            }

            Date expiryDate = jwtTokenUtil.getExpirationDateFromToken(token);
            tokenBlacklistStorage.blacklistToken(token, username, expiryDate.toInstant());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token: " + e.getMessage());
        }
    }

    public void register(PersonRegistrationDto dto) {
        if (personRepository.existsByEmail(dto.email()) ||
                personRepository.existsByUserUsername(dto.username())) {
            throw new IllegalArgumentException("Email or username already exists");
        }

        Person person = new Person(dto.name(), dto.email());
        User user = new User(dto.username(), passwordEncoder.encode(dto.password()));
        user.addRole(Role.USER);
        person.setUser(user);

        personRepository.save(person);
    }
}
