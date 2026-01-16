package se.lexicon.pressflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor

@ToString(exclude = "todo")
@EqualsAndHashCode(exclude = "todo")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data; // Store the file content

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;


    public Attachment(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;

        // Sync the other side if not already present
        if (todo != null) {
            todo.getAttachments().add(this);
        }
    }

    public void setArticle(Article article) {
        this.article = article;
    }


}
