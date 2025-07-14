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

    @Column(nullable = false)
    private Long recommendCount = 0L;  // ⭐ 기본값 설정!




    @Column(name = "image_path")
    private String imagePath; // 🔸 실제 저장된 이미지 경로


    @CreationTimestamp
    private LocalDateTime createdAt;

    private String category; // ← 반드시 필요!

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public boolean isHasImage() {
        return imagePath != null && !imagePath.equals("no.jpg");
    }
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
        if(article.recommendCount != null){
            log.info("this.recommendCount : " + this.recommendCount +", article.recommend_count= " + article.recommendCount);
            this.recommendCount = article.recommendCount;
        }

        if(article.bookmarks != null){
            log.info("this.bookmarks : " + this.bookmarks +", article.bookmarks= " + article.bookmarks);
            this.bookmarks= article.bookmarks;
        }




    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addBookmark(Bookmark bookmark) {
        if (this.bookmarks == null) {
            this.bookmarks = new ArrayList<>();
        }
        this.bookmarks.add(bookmark);
        bookmark.setArticle(this); // 양방향 연결
    }



}