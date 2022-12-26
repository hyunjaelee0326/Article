package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j // 로깅을 위한 골뱅이
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticles() {

        return "articles/new"; //templates/greetings
    }

    @PostMapping("/articles/create")
    public String createArtibcle(ArticleForm form) {
        log.info(form.toString());

        // 1. Dto를 Entity로 변환
        Article article = form.toEntity();
        log.info(article.toString());

        // 2. Repository에게 Entity를 DB안에 저장하게 함
        Article saved = articleRepository.save(article);
        log.info(saved.toString());

        return "redirect:/articles/"+saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id=" + id);

        // id로 데이터를 가져옴!
        //Optional<Article> articlEntity = articleRepository.findById(id);
        Article articlEntity = articleRepository.findById(id).orElse(null);

        // id로 가져온 데이터를 모델에 등록
        model.addAttribute("article",articlEntity);

        // 보여줄 페이지를 설정
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        // 모든 article을 가져온다
        //Iterable<Article> articleEntityList = articleRepository.findAll();
        //List<Article> articleEntityList = (List<Article>) articleRepository.findAll();
        List<Article> articleEntityList = articleRepository.findAll();

        // 가져온 article 묶음을 뷰로 전달한다
        model.addAttribute("articleEntityList",articleEntityList);

        // 뷰 페이지를 설정
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){

        // 수정할 데이터를 가져온다
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터 등록
        model.addAttribute("article",articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());

        // DTO를 엔티티로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 엔티티를 DB로 저장
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target != null){
            articleRepository.save(articleEntity); // 엔티티가 db로 갱신
        }

        // 수정결과페이지로 리다이렉트

        return "redirect:/articles/"+articleEntity.getId();
    }

    //@DeleteMapping("/articles/{id}/delete")
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        log.info("삭제요청");
        // 삭제 대상을 가져온다
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 대상을 삭제한다
        if (target != null){
            articleRepository.delete(target);
            // 리다이렉트 시 전달.
            rttr.addFlashAttribute("msg","삭제가 완료됨");
        }
        log.info(target.toString());

        // 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}
