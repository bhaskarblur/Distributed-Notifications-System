package com.bhaskarblur.notification.Models;

public class NotificationMessage {

    private String txnId;
    private NotificationModel notificationModel;

    public NotificationMessage() {
        
    }

    public NotificationMessage(String txnId, NotificationModel notificationModel) {
        this.txnId = txnId;
        this.notificationModel = notificationModel;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
