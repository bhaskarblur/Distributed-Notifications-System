package com.bhaskarblur.notification.Services;

import com.bhaskarblur.notification.Kafka.IKafkaConsumers;
import com.bhaskarblur.notification.Kafka.MessageConsumer;
import com.bhaskarblur.notification.Models.NotificationMessage;
import com.bhaskarblur.notification.Models.NotificationModel;
import com.bhaskarblur.notification.Repositories.NotificationRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;

public class NotificationService implements IKafkaConsumers {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository repository;
    private Gson gson;
    private final MessageConsumer messageConsumer;

    // Responsible for sending updates to SSE
    private iSSEInteractor sseInteractor;

    public void setSseInteractor(iSSEInteractor sseInteractor) {
        this.sseInteractor = sseInteractor;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Autowired
    public NotificationService(NotificationRepository repository, MessageConsumer messageConsumer) {
        this.repository = repository;
        this.messageConsumer = messageConsumer;

        // Subscribe to Msg Consumers
        messageConsumer.subscribeToNotifications(this);
    }

    private void saveNotification(String notificationBody) throws Exception {
        try {
            NotificationMessage notificationMessage = gson.fromJson(notificationBody, NotificationMessage.class);

            logger.info("Received saveNotification Request Txn: {}", notificationMessage.getTxnId());

            if(!sseInteractor.hasTxmEmitter(notificationMessage.getTxnId())) {
                logger.info("Not continuing the request as Txn emitter not found with the id: {}", notificationMessage.getTxnId());
                return;
            }
            notificationMessage.getNotificationModel().setCreatedAt(new Date());
            notificationMessage.setNotificationModel(repository.saveNotification(notificationMessage.getNotificationModel()));

            logger.info("Saved Notification ID: {}", notificationMessage.getNotificationModel().getId());

            // Need to notify the listening SSE connection.
            logger.info("Notifying front end via SSE, Txn Id: {}", notificationMessage.getTxnId());

            if (sseInteractor != null) {
                sseInteractor.updateSSE(notificationMessage.getTxnId(), notificationMessage.getNotificationModel());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error saving notification: " + e.getMessage(), e);
        }
    }

    public List<NotificationModel> getNotificationsByUserId(String userId) throws Exception {
        try {
            logger.info("Received getNotificationsByUserId Request: {}", userId);

            return repository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting notifications: " + e.getMessage(), e);
        }
    }

    @Override
    public void Notify(String topic, String message) {
        try {
            saveNotification(message);
        } catch (Exception e) {
            logger.error("Notification Service message: {} threw error: {}", topic, e.getMessage());
        }
    }

    @PreDestroy
    public void cleanup() {
        messageConsumer.unSubscribeToNotifications(this);
        logger.info("Unsubscribed from notifications on service shutdown");
    }
}
