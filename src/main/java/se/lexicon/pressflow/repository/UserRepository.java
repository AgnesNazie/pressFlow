package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.pressflow.model.User;

public interface UserRepository  extends JpaRepository <User, Long> {
}
