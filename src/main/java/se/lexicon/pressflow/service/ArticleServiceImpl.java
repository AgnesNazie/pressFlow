package se.lexicon.pressflow.service;

import org.springframework.stereotype.Service;
import se.lexicon.pressflow.entity.Article;
import se.lexicon.pressflow.entity.ArticleStatus;
import se.lexicon.pressflow.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article createArticle(Article article) {
        // you can set defaults here if needed, e.g., status = DRAFT
        if (article.getStatus() == null) {
            article.setStatus(ArticleStatus.DRAFT);
        }
        return articleRepository.save(article);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
    public Article updateArticle(Article article) {
        // Optional: check if the article exists
        if (!articleRepository.existsById(article.getId())) {
            throw new RuntimeException("Cannot update. Article not found with id: " + article.getId());
        }
        return articleRepository.save(article);

    }

    @Override
    public Article getArticleById(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElseThrow(() ->
                new RuntimeException("Article not found with id " + id));
    }
    @Override
    public Article submitArticle(Long id) {
        Article article = getArticleById(id);
        article.setStatus(ArticleStatus.UNDER_REVIEW); // change status
        return articleRepository.save(article);
    }
    @Override
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Article not found with id: " + id);
        }
        articleRepository.deleteById(id);

    }
}
