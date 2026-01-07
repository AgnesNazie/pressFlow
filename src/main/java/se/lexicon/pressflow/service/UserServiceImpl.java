package se.lexicon.pressflow.service;

import org.springframework.stereotype.Service;
import se.lexicon.pressflow.entity.User;
import se.lexicon.pressflow.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
