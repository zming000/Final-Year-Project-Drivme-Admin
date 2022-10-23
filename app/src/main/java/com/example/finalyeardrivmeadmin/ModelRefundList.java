package com.example.finalyeardrivmeadmin;

public class ModelRefundList {
    //declare variables
    String orderID, refundStatus;

    public ModelRefundList() {/*empty constructor*/
    }

    public ModelRefundList(String orderID, String refundStatus) {
        this.orderID = orderID;
        this.refundStatus = refundStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }
}
