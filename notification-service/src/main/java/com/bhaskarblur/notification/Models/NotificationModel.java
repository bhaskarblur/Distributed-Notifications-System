package com.bhaskarblur.notification.Models;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationModel {

    private String id;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime createdAt;

    // Constructor to auto-generate an ID and set createdAt
    public NotificationModel(String userId, String title, String description) {
        this.id = UUID.randomUUID().toString(); // Generate a unique ID
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now(); // Set creation time to now
    }

    // Optional constructor for full control over properties
    public NotificationModel(String id, String userId, String title, String description, LocalDateTime createdAt) {
        this.id = id != null ? id : UUID.randomUUID().toString(); // Use provided ID or generate one
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now(); // Use provided date or set to now
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public NotificationModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
