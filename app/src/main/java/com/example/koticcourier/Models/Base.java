package com.example.koticcourier.Models;

import com.example.koticcourier.Components.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class Base {
    protected int ID;
    protected String requestDate;
    protected boolean isDeleted;

    public Base() {
        this.ID = ID;
        this.requestDate = DateUtils.getDateString(Calendar.getInstance().getTime());
        this.isDeleted = false;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRequestDate() {
        if (requestDate != null){
            return DateUtils.getDateString(requestDate);
        }

        return "";
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
