package se.lexicon.pressflow.service;

import se.lexicon.pressflow.entity.User;

public interface UserService {
    User findByUsername(String username);
}
