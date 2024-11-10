package com.bhaskarblur.order.Models;

public class OrderMessage {
    private  String txnId;
    private OrderModel orderModel;

    public OrderMessage() {
    }

    public OrderMessage(String txnId, OrderModel orderModel) {
        this.txnId = txnId;
        this.orderModel = orderModel;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }
}
