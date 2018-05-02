package com.github.holyloop.article.trending.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.holyloop.article.trending.rest.controller.ArticleController;

@SpringBootApplication(scanBasePackageClasses = { ArticleController.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
