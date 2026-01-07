package se.lexicon.pressflow.service;


import se.lexicon.pressflow.entity.Article;

import java.util.List;


public interface ArticleService {

    Article createArticle(Article article);
    List<Article> getAllArticles();
    Article updateArticle(Article article);
    Article getArticleById(Long id);
    void deleteArticle(Long id);
    Article submitArticle(Long id);
}
