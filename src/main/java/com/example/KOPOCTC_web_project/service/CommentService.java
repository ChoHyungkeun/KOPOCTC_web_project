package com.example.KOPOCTC_web_project.service;

import com.example.KOPOCTC_web_project.dto.CommentDto;
import com.example.KOPOCTC_web_project.entity.Article;
import com.example.KOPOCTC_web_project.entity.Comment;
import com.example.KOPOCTC_web_project.repository.ArticleRepository;
import com.example.KOPOCTC_web_project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CommentService {
    @Autowired
    public CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;


    public List<CommentDto> comments(Long articleId) {
        //댓글을 db에서 조회
        List<Comment> comments = commentRepository.findByArticleId(articleId);


        //엔티티를 dto로 변환하고
//        List<CommentDto> dtos = new ArrayList<CommentDto>();
//        for(int i = 0; i < comments.size(); i++) {
//            Comment comment = comments.get(i);
//            CommentDto dto = CommentDto.createCommentDto(comment);
//            dtos.add(dto);
//        }

        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment -> CommentDto.createCommentDto(comment))
                .collect(Collectors.toList());


        //반환

//        return dtos;
    }
    @Transactional
    public CommentDto createComment(Long articleId, CommentDto commentDto) {
        //게시글이 존재하는지 조회
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));


        //댓글을 엔티티로 생성하고
        Comment comment = Comment.createComment(commentDto, article);


        //엔티티를 db에 저장
        Comment saved = commentRepository.save(comment);

        //dto로 변환해서 반환
        return  CommentDto.createCommentDto(saved);
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto) {


        //댓글조회
        Comment saved = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 댓글이 없어요"));

        //public return형 함수명(파라미터){내용}
        //throw(() - 함수명이 없는 함수 // -> return의 형식을 적는 것
        // ㄴ 이름 없는 함수를 만들어서 return을 받기 위한것
        //댓글 수정
        saved.patch(commentDto); //오리지널

        //db 업데이트
        Comment updated = commentRepository.save(saved);

        //dto변화 후 응답
        return CommentDto.createCommentDto(updated);
    }
    @Transactional
    public CommentDto deleteComment(Long id) {
        Comment saved = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글삭제 실패, 대상없음"));

        commentRepository.delete(saved);

        return CommentDto.createCommentDto(saved);
    }
}
