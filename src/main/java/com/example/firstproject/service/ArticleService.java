package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Service 선언 ( 서비스 객체를 스프링 부트에 생성)
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;


    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        // 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id= {},art= {}",id,article.toString());

        Article target = articleRepository.findById(id).orElse(null);

        if (target == null || id != article.getId()){
            log.info("잘못된 요청 , id= {},art= {}",id,article.toString());
            return null; // 400
        }

        target.patch(article);
        Article updated = articleRepository.save(article);
        return updated;
    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null);

        if (target == null){
            log.info("잘못된 요청 , id= {},art= {}",id,target.toString());
            return null; // 400
        }
        articleRepository.delete(target);
        //https://www.youtube.com/watch?v=l5Va-XbwR04&list=PLyebPLlVYXCiYdYaWRKgCqvnCFrLEANXt&index=21

        return target;
    }
}
