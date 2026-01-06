package se.lexicon.pressflow.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Articles authored by Reporter
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Article> articlesAuthored;

    // Articles edited by Editor
    @OneToMany(mappedBy = "editor", cascade = CascadeType.ALL)
    private List<Article> articlesEdited;

    // Getters & Setters
    // (or use Lombok @Data)
}
