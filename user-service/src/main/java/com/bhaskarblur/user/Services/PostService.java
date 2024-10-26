package com.bhaskarblur.user.Services;

import com.bhaskarblur.user.Api.Dtos.CreatePostRequest;
import com.bhaskarblur.user.Models.PostModel;
import com.bhaskarblur.user.Repositories.PostRepository;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PostService {

    private final PostRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public PostModel createPost(CreatePostRequest postRequest) throws Exception {
        try {
            logger.info("Creating post for user: {}", postRequest.getUser_id());

            logger.debug("Post details: title={}, content={}, description={}",
                    postRequest.getTitle(), postRequest.getContent(), postRequest.getDescription());

            // Mocked error return;
            if (postRequest.getUser_id().contains("1234")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("%s is a fake user, use a real one", postRequest.getUser_id())
                );
            }
            PostModel post = new PostModel()
                    .setUserId(postRequest.getUser_id())
                    .setTitle(postRequest.getTitle())
                    .setContent(postRequest.getContent())
                    .setDescription(postRequest.getDescription());

            return repository.createPost(post);
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating post: " + e.getMessage(), e);
        }
    }
}
