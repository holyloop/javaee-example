package com.github.holyloop.article.trending.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.holyloop.article.trending.entity.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String> {

}
