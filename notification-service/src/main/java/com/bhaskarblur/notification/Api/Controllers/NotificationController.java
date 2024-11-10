package com.bhaskarblur.notification.Api.Controllers;

import com.bhaskarblur.notification.Api.Dtos.ApiStandardResponse;
import com.bhaskarblur.notification.Models.NotificationModel;
import com.bhaskarblur.notification.Services.iSSEInteractor;
import com.bhaskarblur.notification.Services.NotificationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/{userId}/notifications")
@Validated
@EnableScheduling
public class NotificationController implements iSSEInteractor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService service;
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
        service.setSseInteractor(this);
        logger.info("NotificationController initialized.");
    }

    @GetMapping
    public ResponseEntity<ApiStandardResponse> getUserNotifications(
            @NotNull @PathVariable @NotBlank(message = "User ID is required")
            @Size(min = 3, max = 20, message = "User ID must be between 3 and 20 characters") String userId) throws Exception {
        logger.debug("Fetching notifications for user ID: {}", userId);
        List<NotificationModel> notifications = service.getNotificationsByUserId(userId);
        logger.debug("Notifications fetched for user ID: {}: {}", userId, notifications);
        ApiStandardResponse response = new ApiStandardResponse(true, "Notifications fetched!", notifications);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/stream-sse/{txnId}")
    public SseEmitter streamSseEvents(@PathVariable String userId, @PathVariable String txnId) {
        logger.debug("Starting SSE stream for txnId: {}", txnId);
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(txnId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(txnId);
            logger.info("SSE stream completed for txnId: {}", txnId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(txnId);
            logger.warn("SSE stream timed out for txnId: {}", txnId);
        });
        emitter.onError((e) -> {
            emitters.remove(txnId);
            logger.error("Error in SSE stream for txnId: {}", txnId, e);
        });

        try {
            ApiStandardResponse response = new ApiStandardResponse(true, "Connection established with Transaction ID: " + txnId, null);
            emitter.send(SseEmitter.event().name("INIT").data(response), MediaType.APPLICATION_JSON);
            logger.debug("Connection established event sent for txnId: {}", txnId);
        } catch (IOException e) {
            emitter.completeWithError(e);
            logger.error("Error sending initial connection event for txnId: {}", txnId, e);
        }

        return emitter;
    }

    @Scheduled(fixedRate = 10000)
    public void sendHeartbeat() {
        logger.debug("Sending heartbeat to all SSE emitters");
        emitters.forEach((txnId, emitter) -> {
            try {
                ApiStandardResponse response = new ApiStandardResponse(true, "Sending connection heartbeat", null);
                emitter.send(SseEmitter.event().name("HEARTBEAT").data(response), MediaType.APPLICATION_JSON);
                logger.debug("Heartbeat sent for txnId: {}", txnId);
            } catch (IOException e) {
                emitters.remove(txnId);
                emitter.completeWithError(e);
                logger.error("Error sending heartbeat for txnId: {}", txnId, e);
            }
        });
    }

    @Override
    public void updateSSE(String txnId, Object data) {
        logger.debug("Updating SSE for txnId: {} with data: {}", txnId, data);
        SseEmitter emitter = emitters.get(txnId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("UPDATE").data(data, MediaType.APPLICATION_JSON));
                logger.debug("Update sent for txnId: {}", txnId);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(txnId);
                logger.error("Error updating SSE for txnId: {}", txnId, e);
            }
        } else {
            logger.warn("No emitter found for txnId: {}", txnId);
        }
    }
}
