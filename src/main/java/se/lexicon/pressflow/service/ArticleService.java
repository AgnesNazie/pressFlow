package se.lexicon.pressflow.service;

import org.springframework.stereotype.Service;
import se.lexicon.pressflow.model.Article;
import se.lexicon.pressflow.repository.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article updateArticle(Article article) {
        return articleRepository.save(article);
    }
}

