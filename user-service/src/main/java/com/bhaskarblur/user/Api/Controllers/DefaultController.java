package com.bhaskarblur.user.Api.Controllers;

import com.bhaskarblur.user.Api.Dtos.ApiStandardResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class DefaultController implements ErrorController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiStandardResponse> healthCheck() {
        return ResponseEntity.ok(new ApiStandardResponse(true, "Service running well!", null));
    }

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            HttpStatus status = HttpStatus.resolve(statusCode);
            if (status == HttpStatus.METHOD_NOT_ALLOWED) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("405 Method Not Allowed - Method does not exist");
            }
        }
        // Default to 405 if no specific status is found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found - This endpoint does not exist");
    }
}
