package com.bhaskarblur.user.Services;

import com.bhaskarblur.user.Api.Dtos.CreatePostRequest;
import com.bhaskarblur.user.Kafka.MessageProducer;
import com.bhaskarblur.user.Models.PostModel;
import com.bhaskarblur.user.Repositories.PostRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class OrderService {

    private final MessageProducer kafkaMessageProducer;
    private Gson gson;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    @Autowired
    public OrderService(MessageProducer kafkaMessageProducer) {
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @Autowired
    public void SetGson(Gson gson) {
        this.gson = gson;
    }

    public Object createOrder(CreatePostRequest postRequest) throws Exception {
        try {
            logger.info("Creating post for user: {}", postRequest.getUser_id());

            logger.info("Post details: title={}, content={}, description={}",
                    postRequest.getTitle(), postRequest.getContent(), postRequest.getDescription());

            // Mocked error return;
            if (postRequest.getUser_id().contains("fake")) {
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


            // Manually set each property of post to notificationPayload
            Map<String, Object> notificationPayload = new HashMap<>();
            notificationPayload.put("type", "POST");
            notificationPayload.put("userId", post.getUserId());
            notificationPayload.put("title", post.getTitle());
            notificationPayload.put("description", post.getDescription());

            // Convert notification payload to JSON String using Gson
            String notificationJsonString = gson.toJson(notificationPayload);
            logger.info("Sending notification to Kafka: {}", notificationJsonString);

            // 2. Send to Kafka Topic via MessageProducer
            kafkaMessageProducer.sendMessage("notification", notificationJsonString);

            return repository.createPost(post);
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating post: " + e.getMessage(), e);
        }
    }
}