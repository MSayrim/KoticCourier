package com.example.koticcourier.Models;

import com.example.koticcourier.Enums.Status;

public class Order extends Base {

    private String orderDetail;
    private Status _status;
    private int userID;
    private int courierID;

    public Order( String orderDetail, Status status, int userID, int courierID) {
        super();
        this.orderDetail = orderDetail;
        this._status = status;
        this.userID = userID;
        this.courierID = courierID;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Status getStatus() {
        return _status;
    }

    public void setStatu(Status status) {
        this._status = status;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCourierID() {
        return courierID;
    }

    public void setCourierID(int courierID) {
        this.courierID = courierID;
    }
}
