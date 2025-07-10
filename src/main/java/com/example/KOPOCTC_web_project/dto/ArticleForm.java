package com.example.KOPOCTC_web_project.dto;

import com.example.KOPOCTC_web_project.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@ToString
@Getter
@Setter

public class ArticleForm {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private MultipartFile imageFile; // 🔸 업로드된 이미지
    private LocalDateTime createdAt;
    private String imagePath;

    public boolean isHasImage() {
        return imagePath != null && !imagePath.equals("no.jpg");
    }
    public Article toEntity(String imagePath) {
        return new Article(id, title, writer, content, imagePath, createdAt, new ArrayList<>());
    }


}


