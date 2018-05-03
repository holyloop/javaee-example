package com.github.holyloop.article.trending.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.holyloop.article.trending.Application;
import com.github.holyloop.article.trending.config.EmbeddedRedisConfig;
import com.github.holyloop.article.trending.entity.Article;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@Import(value = { EmbeddedRedisConfig.class })
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;

    @Test
    public void testSaveOne() {
        Article article = new Article();
        String id = "1";
        article.setId(id);
        repository.save(article);

        Article articleInRedis = repository.findById(id).get();
        assertNotNull(articleInRedis);
    }

}
