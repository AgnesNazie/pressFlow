package se.lexicon.pressflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "username")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    private boolean expired;

    @OneToOne(mappedBy = "user")
    private Person person;

    @OneToMany(mappedBy = "user")
    private List<TodoAssignment> assignments = new ArrayList<>();



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Enumerated(EnumType.STRING)
    private Role systemRole;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime lastLogin;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person AccountStatusperson;

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
