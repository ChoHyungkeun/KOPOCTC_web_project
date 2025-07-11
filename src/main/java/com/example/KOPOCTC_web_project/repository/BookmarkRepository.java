package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.entity.Article;
import com.example.KOPOCTC_web_project.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // 특정 사용자(userKey)가 특정 글(article)을 즐겨찾기 했는지 확인
    Optional<Bookmark> findByArticleAndUserKey(Article article, String userKey);

    // 특정 사용자(userKey)의 모든 즐겨찾기 목록 조회
    List<Bookmark> findAllByUserKey(String userKey);

    // 특정 사용자(userKey)가 특정 글(articleId)를 즐겨찾기 했는지 확인
    boolean existsByArticleIdAndUserKey(Long articleId, String userKey);

    // 즐겨찾기 삭제
    void deleteByArticleIdAndUserKey(Long articleId, String userKey);
}
