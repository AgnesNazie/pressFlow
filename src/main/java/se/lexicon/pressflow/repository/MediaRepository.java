package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.MediaRole;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaRole, Long> {

    Optional<MediaRole> findByRoleName(String roleName);
}
