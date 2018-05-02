package com.github.holyloop.basic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class Config {

    @Bean
    public CompactDisc compactDisc() {
        return new CompactDisc();
    }

    @Bean
    public PlayTracker playTracker() {
        return new PlayTracker();
    }

}
