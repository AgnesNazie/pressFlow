package se.lexicon.pressflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "todo_assignment")
@NoArgsConstructor
public class TodoAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @Enumerated(EnumType.STRING)
    private Responsibility responsibility;

    @ManyToOne
    private Todo todo;

    @ManyToOne
    private User user;

    private LocalDateTime assignedAt = LocalDateTime.now();
}
