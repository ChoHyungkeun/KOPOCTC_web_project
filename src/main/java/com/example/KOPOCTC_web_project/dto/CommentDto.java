package com.example.KOPOCTC_web_project.dto;

import com.example.KOPOCTC_web_project.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

public class CommentDto {
    private Long id;
    private Long articleId;
    private String writer;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(), // 댓글의 인덱스
                comment.getArticle().getId(), //댓글이 바라보는 게시글의 id
                comment.getWriter(), //댓글 작성자ㅑ
                comment.getBody() //댓글 내용

        );

    }


}
