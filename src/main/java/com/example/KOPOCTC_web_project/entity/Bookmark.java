package com.example.KOPOCTC_web_project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Bookmark {@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne  // 다대일 관계: 여러 Bookmark가 하나의 Article을 참조
    private Article article;
    private String userKey;

}
