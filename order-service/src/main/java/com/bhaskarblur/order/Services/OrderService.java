package com.bhaskarblur.order.Services;

import com.bhaskarblur.order.Api.Dtos.CreateOrderRequest;
import com.bhaskarblur.order.Kafka.MessageProducer;
import com.bhaskarblur.order.Repositories.OrderRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderService {

    private final OrderRepository repository;
    private final MessageProducer kafkaMessageProducer;
    private Gson gson;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);


    @Autowired
    public OrderService(OrderRepository repository, MessageProducer kafkaMessageProducer) {
        this.repository = repository;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @Autowired
    public void SetGson(Gson gson) {
        this.gson = gson;
    }

    public PostModel createPost(CreateOrderRequest postRequest) throws Exception {
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


            // 1. Convert post to JSON String using Gson
            String postJsonString = gson.toJson(post);
            logger.info("Sending post to Kafka: {}", postJsonString);

            // 2. Send to Kafka Topic via MessageProducer
            kafkaMessageProducer.sendMessage("notification", postJsonString);

            return repository.createPost(post);
        }
        catch (Exception e) {
            throw new RuntimeException("Error creating post: " + e.getMessage(), e);
        }
    }
}