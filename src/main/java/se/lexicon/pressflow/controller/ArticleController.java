package se.lexicon.pressflow.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.pressflow.entity.Article;
import se.lexicon.pressflow.service.ArticleService;

import java.util.List;
@RestController
@RequestMapping("/api/articles")
@Validated
@SecurityRequirement(name = "Bearer Authentication")  // Swagger security
@Tag(name = "Article API", description = "API endpoints for managing articles")
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Create a new article (any authenticated user)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201 Created
    public Article createArticle(@RequestBody @NotNull @Valid Article article) {
        System.out.println("Creating article: " + article.getTitle());
        return articleService.createArticle(article);
    }

    // Get all articles (any authenticated user)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK) // 200 OK
    public List<Article> getAllArticles() {
        System.out.println("Fetching all articles");
        return articleService.getAllArticles();
    }

    // Get article by ID
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // 200 OK
    public Article getArticleById(
            @PathVariable("id")
            @Positive(message = "Id must be a positive number")
            Long id) {
        System.out.println("Fetching article with ID: " + id);
        return articleService.getArticleById(id);
    }

    // Submit article for review
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/submit")
    @ResponseStatus(HttpStatus.OK) // 200 OK
    public Article submitArticle(
            @PathVariable("id")
            @Positive(message = "Id must be a positive number")
            Long id) {
        System.out.println("Submitting article with ID: " + id);
        return articleService.submitArticle(id);
    }

    // Update an article (only ADMIN or the author)
    @PreAuthorize("hasRole('ADMIN') or #article.authorId == authentication.principal.username")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void updateArticle(
            @PathVariable("id")
            @Positive(message = "Id must be a positive number") Long id,
            @RequestBody @NotNull @Valid Article article) {
        System.out.println("Updating article with ID: " + id);
        articleService.updateArticle(article);
    }

    // Delete an article (only ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    public void deleteArticle(
            @PathVariable("id")
            @Positive(message = "Id must be a positive number") Long id) {
        System.out.println("Deleting article with ID: " + id);
        articleService.deleteArticle(id);
    }
}