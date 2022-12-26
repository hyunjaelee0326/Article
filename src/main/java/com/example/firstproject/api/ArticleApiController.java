package com.example.firstproject.api;


import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
public class ArticleApiController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/api/articles")
    public List<Article> index(){
        return articleRepository.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public Article indexById(@PathVariable Long id){
        return articleRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto){ // json data 받기
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id, // ResponseEntity 상태코드 담김
                                          @RequestBody ArticleForm dto){ // json data 받기
        // 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id= {},art= {}",id,article.toString());

        Article target = articleRepository.findById(id).orElse(null);

        if (target == null || id != article.getId()){
            log.info("잘못된 요청 , id= {},art= {}",id,article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400
        }

        target.patch(article);
        Article updated = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id){

        Article target = articleRepository.findById(id).orElse(null);

        if (target == null){
            log.info("잘못된 요청 , id= {},art= {}",id,target.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400
        }
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
