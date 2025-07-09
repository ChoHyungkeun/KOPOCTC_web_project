package com.example.KOPOCTC_web_project.entity;

import com.example.KOPOCTC_web_project.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="article_id") // 외래키 선언, Article 엔티티의 id와 매핑작업 엄밀히 말하면 article_id가 컬럼명이 된다.
    //Article 클래스 생성시 이미 id가 pk임을 명시했기 떄문에 따로 id를 보라는 설정을 할 필요가 없다.
    @JsonIgnore  // 🔥 핵심

    private Article article;
    @Column
    private String writer;
    @Column
    private String body;

    public static Comment createComment(CommentDto commentDto, Article article) {
        if(commentDto.getId() != null)
            throw new IllegalArgumentException("댓글의 id가 존재하여 생성에 실패하였습니다.");
        if(commentDto.getArticleId() != article.getId())
            throw new IllegalArgumentException("요청id가 잘못되어 생성에 실패하였습니다.");
        return new Comment(
                commentDto.getId(),
                article,
                commentDto.getWriter(),
                commentDto.getBody()
        );
    }


    public void patch(CommentDto dto) {
        if(this.id != dto.getId())
            throw new IllegalArgumentException("잘못된 id의 입력으로 예외를 발생시킵니다.");
        //입력한 아이디과 글의 아이디가 다르면 오류

        if(dto.getWriter() != null)
            this.writer = dto.getWriter();
        //오리지널에서 수정된 부분만 저장
        if(dto.getBody() != null)
            this.body = dto.getBody();
        //마찬가지



    }
}