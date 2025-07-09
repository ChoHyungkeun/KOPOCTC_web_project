package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //특정 게시물의 모든 댓글 조회
    @Query(value="select * from comment where article_id = :articleId", nativeQuery = true)//변수라는 표시 :
    List<Comment> findByArticleId(Long articleId);


    //특정 닉네임의 모든 댓글 조회
    List<Comment> findByWriter(String writer);



}
