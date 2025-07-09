package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    @Override
    ArrayList<Article> findAll();
}

