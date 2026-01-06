package se.lexicon.pressflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.pressflow.model.Article;

public interface ArticleRepository  extends JpaRepository <Article, Long>  {
}
