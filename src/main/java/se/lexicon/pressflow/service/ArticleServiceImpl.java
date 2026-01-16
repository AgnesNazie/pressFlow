package se.lexicon.pressflow.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.pressflow.dto.ArticleCreateDto;
import se.lexicon.pressflow.dto.ArticleDto;
import se.lexicon.pressflow.dto.ArticleUpdateDto;
import se.lexicon.pressflow.dto.AttachmentDto;
import se.lexicon.pressflow.entity.Article;
import se.lexicon.pressflow.entity.ArticleStatus;
import se.lexicon.pressflow.entity.Attachment;
import se.lexicon.pressflow.entity.User;
import se.lexicon.pressflow.repository.ArticleRepository;
import se.lexicon.pressflow.repository.AttachmentRepository;
import se.lexicon.pressflow.repository.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              UserRepository userRepository,
                              AttachmentRepository attachmentRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public ArticleDto create(ArticleCreateDto dto, MultipartFile[] files, Principal principal) {
        // Find the author by logged-in user
        User author = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map DTO to entity
        Article article = new Article();
        article.setTitle(dto.title());
        article.setContent(dto.content());
        article.setStatus(ArticleStatus.DRAFT); // initial status
        article.setAuthor(author);

        // Save the article first
        article = articleRepository.save(article);

        // Save attachments if any
        if (files != null) {
            for (MultipartFile file : files) {
                Attachment attachment = new Attachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileType(file.getContentType());
                try {
                    attachment.setData(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to store attachment: " + file.getOriginalFilename(), e);
                }
                article.addAttachment(attachment);
            }
        }

        return convertToDto(article);
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDto findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return convertToDto(article);
    }

    @Override
    public ArticleDto submit(Long id, Principal principal) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        // Check if the logged-in user is the author
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new RuntimeException("You are not allowed to submit this article");
        }

        // Only allow submitting DRAFT articles
        if (article.getStatus() != ArticleStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT articles can be submitted");
        }

        // Update status and submission time
        article.setStatus(ArticleStatus.UNDER_REVIEW);
        article.setSubmittedAt(LocalDateTime.now());

        // Persist changes
        Article updated = articleRepository.save(article);

        return convertToDto(updated);
    }

    @Override
    public void update(Long id, ArticleUpdateDto dto,  MultipartFile[] files,
                       Principal principal) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new RuntimeException("You can only edit your own article");
        }

        if (article.getStatus() != ArticleStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT articles can be edited");
        }
        article.setTitle(dto.title());
        article.setContent(dto.content());

        // Optionally update status if your design allows (usually not needed here)
        if (dto.status() != null) {
            article.setStatus(dto.status());
        }

        // handle new attachments
        if (files != null) {
            for (MultipartFile file : files) {
                Attachment attachment = new Attachment();
                attachment.setFileName(file.getOriginalFilename());
                attachment.setFileType(file.getContentType());
                try {
                    attachment.setData(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file", e);
                }
                article.addAttachment(attachment);
            }
        }
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    private ArticleDto convertToDto(Article article) {
        List<AttachmentDto> attachments = article.getAttachments().stream()
                .map(att -> new AttachmentDto(att.getId(), att.getFileName(), att.getFileType(), att.getData()))
                .collect(Collectors.toList());

        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .status(article.getStatus())
                .submittedAt(article.getSubmittedAt())
                .publishedAt(article.getPublishedAt())
                .authorUsername(article.getAuthor() != null ? article.getAuthor().getUsername() : null)
                .editorUsername(article.getEditor() != null ? article.getEditor().getUsername() : null)
                .attachments(attachments)
                .numberOfAttachments(attachments.size())
                .build();
    }
}
