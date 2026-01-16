package se.lexicon.pressflow.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.pressflow.dto.ArticleCreateDto;
import se.lexicon.pressflow.dto.ArticleDto;
import se.lexicon.pressflow.dto.ArticleUpdateDto;
import se.lexicon.pressflow.entity.Article;
import se.lexicon.pressflow.service.ArticleService;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/api/articles")
@Validated
@SecurityRequirement(name = "Bearer Authentication")  // Swagger security
@Tag(name = "Article API", description = "API endpoints for managing articles, attachments, and submissions")
public class ArticleController {


    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // Create a new article (any authenticated user)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto createArticle(
            @RequestPart("article") @Valid ArticleCreateDto articleDto,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            Principal principal
    ) {
        return articleService.create(articleDto, files, principal);
    }

    // 📄 Get all articles
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleDto> getAllArticles() {
        return articleService.findAll();
    }

    // 🔍 Get article by ID
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleDto getArticleById(
            @PathVariable @Positive Long id
    ) {
        return articleService.findById(id);
    }

    // 📤 Submit article for review
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/submit")
    @ResponseStatus(HttpStatus.OK)
    public ArticleDto submitArticle(
            @PathVariable @Positive Long id,
            Principal principal
    ) {
        return articleService.submit(id, principal);
    }

    // ✏️ Update article (author only)
    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArticle(
            @PathVariable @Positive Long id,
            @RequestPart("article") @Valid ArticleUpdateDto articleDto,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            Principal principal
    ) {
        articleService.update(id, articleDto, files, principal);
    }

    // 🗑 Delete article (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(
            @PathVariable @Positive Long id
    ) {
        articleService.delete(id);
    }

}