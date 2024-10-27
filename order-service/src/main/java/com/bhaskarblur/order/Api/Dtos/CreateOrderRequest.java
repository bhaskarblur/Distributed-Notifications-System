package com.bhaskarblur.order.Api.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateOrderRequest {

    @NotNull(message = "User id is required")
    private String user_id;


}
