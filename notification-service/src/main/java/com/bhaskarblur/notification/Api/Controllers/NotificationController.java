package com.bhaskarblur.notification.Api.Controllers;

import com.bhaskarblur.notification.Api.Dtos.ApiStandardResponse;
import com.bhaskarblur.notification.Models.NotificationModel;
import com.bhaskarblur.notification.Services.iSSEInteractor;
import com.bhaskarblur.notification.Services.NotificationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private final NotificationService service;
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
        // Initializing the SSE interactor
        service.setSseInteractor(this);
    }

    @GetMapping
    public ResponseEntity<ApiStandardResponse> getUserNotifications(
            @NotNull @PathVariable @NotBlank(message = "User ID is required")
            @Size(min = 3, max = 20, message = "User ID must be between 3 and 20 characters") String userId) throws Exception {

        List<NotificationModel> notifications = service.getNotificationsByUserId(userId);
        ApiStandardResponse response = new ApiStandardResponse(
                true, "Notifications fetched!", notifications
        );
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/stream-sse/{txnId}")
    public SseEmitter streamSseEvents(@PathVariable String userId, @PathVariable String txnId) {
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(txnId, emitter);

        emitter.onCompletion(() -> emitters.remove(txnId));
        emitter.onTimeout(() -> emitters.remove(txnId));
        emitter.onError((e) -> emitters.remove(txnId));

        // Send a connected event immediately on connection
        try {
            ApiStandardResponse response = new ApiStandardResponse(true, "Connection established with Transaction ID: " + txnId, null);
            emitter.send(SseEmitter.event().name("INIT").data(response), MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @Scheduled(fixedRate = 10000)
    public void sendHeartbeat() {
        emitters.forEach((txnId, emitter) -> {
            try {
                ApiStandardResponse response = new ApiStandardResponse(true, "Sending connection heartbeat", null);
                emitter.send(SseEmitter.event().name("HEARTBEAT").data(response), MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitters.remove(txnId);
                emitter.completeWithError(e);
            }
        });
    }

    @Override
    public void updateSSE(String txnId, Object data) {
        SseEmitter emitter = emitters.get(txnId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("UPDATE").data(data, MediaType.APPLICATION_JSON));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(txnId);
            }
        }
    }
}
