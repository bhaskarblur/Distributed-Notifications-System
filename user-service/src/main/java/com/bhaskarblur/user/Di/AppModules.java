package com.bhaskarblur.user.Di;

import com.bhaskarblur.user.Repositories.PostRepository;
import com.bhaskarblur.user.Services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppModules {
    private static final Logger logger = LoggerFactory.getLogger(AppModules.class);
    @Bean
    public PostRepository injectPostRepository() {
        logger.info("🚀 Initialized Post Repository");
        return new PostRepository();
    }

    @Bean
    public PostService injectPostService(PostRepository repository) {
        logger.info("🚀 Initialized Post Service");
        return new PostService(repository);
    }
}
