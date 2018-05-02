package com.github.holyloop.article.trending.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping
    public ResponseEntity<String> greet() {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }

}
