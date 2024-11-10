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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/{userId}/notifications")
@Validated

public class NotificationController implements iSSEInteractor {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService service;
    private final ConcurrentHashMap<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

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
        List<NotificationModel> notifications = service.getNotificationsByUserId(userId);
        return ResponseEntity.ok(new ApiStandardResponse(true, "Notifications fetched!", notifications));
    }

    @GetMapping("/stream-sse")
    public SseEmitter connectToSSE(@NotNull @PathVariable @NotBlank(message = "User ID is required")
                                   @Size(min = 3, max = 20, message = "User ID must be between 3 and 20 characters") String userId, @RequestParam("txnId") String txnId) {

        SseEmitter emitter = new SseEmitter(300_000L);
        sseEmitters.put(txnId, emitter);

        // Remove emitter on completion, timeout, or error
        emitter.onCompletion(() -> sseEmitters.remove(txnId));
        emitter.onTimeout(() -> sseEmitters.remove(txnId));
        emitter.onError(e -> sseEmitters.remove(txnId));

        // Send initial connection confirmation
        try {
            logger.info("Connected to SSE with txnId: {}", txnId);
            ApiStandardResponse response = new ApiStandardResponse(
                    true,
                    "Connected to SSE for txnId: " + txnId,
                    null
            );
            emitter.send(response);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        // Optional: send heartbeat to keep connection active
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().data(new ApiStandardResponse(
                        true,
                        "Sending connection alive heart-beat",
                        null)));
            } catch (IOException e) {
                emitter.completeWithError(e);
                executor.shutdown(); // Shut down the heartbeat if connection fails
            }
        }, 0, 15, TimeUnit.SECONDS); // Ping every 15 seconds

        return emitter;
    }


    @Override
    public boolean hasTxmEmitter(String txnId) {
        SseEmitter emitter = sseEmitters.get(txnId);
        return emitter != null;
    }

    @Override
    public void updateSSE(String txnId, Object data) {
        SseEmitter emitter = sseEmitters.get(txnId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("UPDATE").data(data, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(txnId);
            }
        }
    }
}
