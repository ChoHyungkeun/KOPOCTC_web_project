package com.example.KOPOCTC_web_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Slf4j
public class Article {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;

    @Column
    private String writer;

    @Column
    private String content;

    @Column(name = "image_path")
    private String imagePath; // 🔸 실제 저장된 이미지 경로

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void patch(Article article) {
        //article.title null이 아니면 this.title 받음
        if(article.title !=null){
            log.info("this.title : " + this.title +", article.title= " + article.title);
            this.title = article.title;
        }
        this.writer = article.writer;

        if(article.content != null){
            log.info("this.content : " + this.content +", article.content= " + article.content);
            this.content = article.content;
        }
        this.imagePath = article.imagePath;


    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }



}