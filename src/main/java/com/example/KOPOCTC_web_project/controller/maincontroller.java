package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.ArticleForm;
import com.example.KOPOCTC_web_project.dto.CommentDto;
import com.example.KOPOCTC_web_project.entity.Article;
import com.example.KOPOCTC_web_project.entity.Bookmark;
import com.example.KOPOCTC_web_project.repository.ArticleRepository;
import com.example.KOPOCTC_web_project.repository.BookmarkRepository;
import com.example.KOPOCTC_web_project.service.CommentService;
import com.example.KOPOCTC_web_project.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.*;

@Controller
@Slf4j
public class maincontroller {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private FileService fileService;


    @PostMapping("/article/create")
    public String create(@ModelAttribute ArticleForm form) throws IOException {
        MultipartFile file = form.getImageFile();
        String fileName = null;

        if (file != null && !file.isEmpty()) {
            fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("uploads", fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        }

        Article article = new Article();
        article.setTitle(form.getTitle());
        article.setWriter(form.getWriter());
        article.setContent(form.getContent());
        article.setImagePath(fileName);
        if (article.getImagePath() == null || article.getImagePath().isBlank()) {
            article.setImagePath("no.jpg");
        }
        Article saved = articleRepository.save(article);

        return "redirect:/text/" + saved.getId();
    }

    @GetMapping("/text/{number}")
    public String txtshow(@PathVariable Long number, Model model, HttpSession session) {
        if (session.getAttribute("userKey") == null) {
            session.setAttribute("userKey", session.getId()); // 세션 ID를 임시키로 사용
        }
        Article saved = articleRepository.findById(number)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + number));
        List<CommentDto> commentDtos = commentService.comments(number);
        // 3. 북마크 여부 확인
        String userKey = (String) session.getAttribute("userKey");
        boolean hasBook = false;

        if (userKey != null) {
            Optional<Bookmark> bookmarkOpt = bookmarkRepository.findByArticleAndUserKey(saved, userKey);
            hasBook = bookmarkOpt.isPresent();
        }

        model.addAttribute("article", saved);
        model.addAttribute("commentDtos", commentDtos);
        model.addAttribute("hasbook", hasBook);  // 추가된 부분

        return "articles/text";
    }
    @GetMapping("/newtext")
    public String newArticleform() {

        return "articles/newtext";
    }
    //새글 컨트롤러

    @GetMapping("/text/list")

    public String text(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {

        List<Article> fullList = articleRepository.findAll(); // 전체 글 목록
        int start = page * size;
        int end = Math.min((start + size), fullList.size());

        List<Article> pagedList = new ArrayList<>();
        if (start <= end) {
            pagedList = fullList.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) fullList.size() / size);

        List<Map<String, Object>> pageNumbers = new ArrayList<>();

        for (int i = 0; i < totalPages; i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i + 1);
            pageInfo.put("numberMinusOne", i);

            pageInfo.put("isCurrentPage", i == page);
            pageNumbers.add(pageInfo);
        }

        model.addAttribute("articles", pagedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);

        return "articles/textlist";
    }
    //글목록 컨트롤러

    @GetMapping("/text/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        Article saved = articleRepository.findById(id).orElse(null);

        model.addAttribute("article", saved);

        return "articles/edit";
    }
    //수정 컨트롤러


    @PostMapping("/text/update")
    public String update(@RequestParam("id") Long id,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "writer", required = false) String writer,
                         @RequestParam(value = "content", required = false) String content,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         @RequestParam(value = "originalImagePath", required = false) String originalImagePath) throws IOException {

        // 1. 기존 데이터 조회
        Article target = articleRepository.findById(id).orElse(null);
        if (target == null) {
            log.warn("해당 ID의 Article이 없습니다: {}", id);
            return "redirect:/error";
        }

        // 2. 이미지 경로 처리
        String imagePath = (imageFile != null && !imageFile.isEmpty())
                ? fileService.saveFile(imageFile)
                : (originalImagePath != null ? originalImagePath : target.getImagePath());

        // 3. null이 아닌 항목만 업데이트
        if (title != null)  target.setTitle(title);
        if (writer != null) target.setWriter(writer);
        if (content != null) target.setContent(content);
        if (imagePath != null) target.setImagePath(imagePath);

        // 4. 저장
        articleRepository.save(target);

        return "redirect:/text/" + id;
    }
    //업데이트 컨트롤러


    @GetMapping("/text/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes rttr){
        Article saved = articleRepository.findById(id).orElse(null);

        if(saved !=null){
            articleRepository.delete(saved);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다.");

        }

        return "redirect:/text/list";

    }
    //삭제 컨트롤러


    @GetMapping("/text/{id}/recommend")
    public String recommend(@PathVariable Long id, HttpSession session, RedirectAttributes rttr) {
        // 이미 추천했는지 확인
        Set<Long> recommended = (Set<Long>) session.getAttribute("recommended");
        if (recommended == null) {
            recommended = new HashSet<>();
        }

        if (recommended.contains(id)) {
            rttr.addFlashAttribute("msg", "이미 추천하셨습니다.");
            return "redirect:/text/" + id;
        }

        // 추천 수 증가
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다. id=" + id));
        Long count = article.getRecommendCount() != null ? article.getRecommendCount() : 0;
        article.setRecommendCount(count + 1);
        articleRepository.save(article);

        // 세션에 추천 기록 추가
        recommended.add(id);
        session.setAttribute("recommended", recommended);

        rttr.addFlashAttribute("msg", "추천 완료!");
        return "redirect:/text/" + id;
    }
    //추천수 컨트롤러

    @PostMapping("/text/{id}/bookmark")
    public String bookmark(@PathVariable Long id,
                           HttpSession session,
                           RedirectAttributes rattr) {

        String userKey = session.getId(); // 나중에: (String) session.getAttribute("userId");

        // 게시글 존재 여부 확인
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            rattr.addFlashAttribute("msg", "해당 게시글이 존재하지 않습니다.");
            return "redirect:/text/list";
        }

        // 이미 즐겨찾기 했는지 확인
        Optional<Bookmark> existing = bookmarkRepository.findByArticleAndUserKey(article, userKey);
        if (existing.isPresent()) {
            rattr.addFlashAttribute("msg", "이미 즐겨찾기한 게시글입니다.");
            return "redirect:/text/" + id;
        }

        // 즐겨찾기 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setArticle(article);  // 꼭 설정
        bookmark.setUserKey(userKey);  // 꼭 설정
        bookmarkRepository.save(bookmark);

// 핵심 추가!
        article.addBookmark(bookmark);

// 저장 (Cascade 설정이 없다면 아래 줄 유지)
        bookmarkRepository.save(bookmark);

        rattr.addFlashAttribute("msg", "즐겨찾기 완료!");
        return "redirect:/text/" + id;
    }

    // 즐겨찾기 해제
    @PostMapping("/text/{id}/unbookmark")
    @Transactional
    public String unbookmark(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes rattr) {

        String userKey = (String) session.getAttribute("userKey");

        // 즐겨찾기 삭제
        bookmarkRepository.deleteByArticleIdAndUserKey(id, userKey);
        rattr.addFlashAttribute("msg", "즐겨찾기 해제 완료!");
        return "redirect:/text/" + id;
    }

}
