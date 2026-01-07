package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository  extends JpaRepository <Person, Long> {
    Optional<Person> findByEmail(String email);

    Optional<Person> findByUserUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUserUsername(String username);
}
