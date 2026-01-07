package se.lexicon.pressflow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class MediaRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaRoleId;

    private String roleName;
}
