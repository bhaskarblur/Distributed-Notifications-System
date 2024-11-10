package com.bhaskarblur.notification.Services;

public interface iSSEInteractor {

    boolean hasTxmEmitter(String txnId);
    void updateSSE(String txnId, Object data);
}
