package se.lexicon.pressflow.service;


import org.springframework.web.multipart.MultipartFile;
import se.lexicon.pressflow.dto.ArticleCreateDto;
import se.lexicon.pressflow.dto.ArticleDto;
import se.lexicon.pressflow.dto.ArticleUpdateDto;

import java.security.Principal;
import java.util.List;


public interface ArticleService {

    ArticleDto create(ArticleCreateDto articleDto, MultipartFile[] files, Principal principal);
    List<ArticleDto> findAll();
    ArticleDto findById(Long id);
    ArticleDto submit(Long id, Principal principal);
    void update(Long id, ArticleUpdateDto articleDto,MultipartFile[] files, Principal principal);
    void delete(Long id);
}
