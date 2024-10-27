package com.bhaskarblur.order.Repositories;

import jakarta.validation.constraints.NotNull;

public class OrderRepository {

    public OrderRepository() {}

    public PostModel createPost(@NotNull PostModel post) {
        // Mocked Database operation
        return post.setId("uuid-postID");
    }
}
