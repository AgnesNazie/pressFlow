package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.Attachment;
import se.lexicon.pressflow.entity.Todo;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachment, Long>{
    List<Attachment> findByTodo(Todo todo);
}
