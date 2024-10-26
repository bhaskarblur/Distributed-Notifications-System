package com.bhaskarblur.notification.Repositories;

import com.bhaskarblur.notification.Models.NotificationModel;
import jakarta.validation.constraints.NotNull;

public class NotificationRepository {

    public NotificationRepository() {}

    public NotificationModel createNotification(@NotNull NotificationModel notificationModel) {
        // Mocked Database operation
        return notificationModel.setId("noti-2024");
    }
}
