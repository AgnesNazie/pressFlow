package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.pressflow.entity.Article;
import se.lexicon.pressflow.entity.ArticleStatus;
import se.lexicon.pressflow.entity.Todo;
import se.lexicon.pressflow.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository  extends JpaRepository <Article, Long> {

    Optional<Article> findByTodo(Todo todo);
    List<Article> findByAuthor(User author);
    List<Article> findByStatus(ArticleStatus status);
}
