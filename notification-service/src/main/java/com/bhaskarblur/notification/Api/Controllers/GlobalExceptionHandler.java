package com.bhaskarblur.notification.Api.Controllers;

import com.bhaskarblur.notification.Api.Dtos.ApiStandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiStandardResponse> handleResponseStatusException(ResponseStatusException ex) {
        ApiStandardResponse response = new ApiStandardResponse(
                false,
                ex.getReason(), // Returns only the custom message without HTTP status details
                null
        );

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
