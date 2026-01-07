package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.Todo;
import se.lexicon.pressflow.entity.TodoAssignment;
import se.lexicon.pressflow.entity.User;

import java.util.List;

@Repository
public interface TodoAssignmentRepository  extends JpaRepository<TodoAssignment, Long> {

    List<TodoAssignment> findByUser(User user);

    List<TodoAssignment> findByTodo(Todo todo);
}
