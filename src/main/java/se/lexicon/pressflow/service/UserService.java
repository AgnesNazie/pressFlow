package se.lexicon.pressflow.service;

import org.springframework.stereotype.Service;
import se.lexicon.pressflow.model.User;
import se.lexicon.pressflow.repository.UserRepository;

import java.util.List;


@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
