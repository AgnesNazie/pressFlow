package se.lexicon.pressflow.repository;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.model.User;
@Repository
public interface UserRepository  extends JpaRepository <User, Long> {
}
