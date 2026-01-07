package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.Role;
import se.lexicon.pressflow.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findByRolesContaining(Role role);

    @Modifying
    @Query("update User u set u.expired = :status where u.username = :username")
    void updateExpiredByUsername(@Param("username") String username, @Param("status") boolean status);

    @Modifying
    @Query("update User u set u.password = :pwd where u.username = :username")
    void updatePasswordByUsername(@Param("username") String username, @Param("pwd") String newPassword);
}
