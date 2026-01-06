package se.lexicon.pressflow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status = ArticleStatus.DRAFT;

    private LocalDateTime submittedAt;
    private LocalDateTime publishedAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "editor_id")
    private User editor;

    // Getters & Setter
}
