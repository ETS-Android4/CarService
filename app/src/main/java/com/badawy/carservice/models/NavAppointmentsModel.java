package com.badawy.carservice.models;

public class NavAppointmentsModel {

    private String serviceLabel,serviceType,price,date,time,address;

    public NavAppointmentsModel(String serviceLabel, String serviceType, String price, String date, String time, String address) {
        this.serviceLabel = serviceLabel;
        this.serviceType = serviceType;
        this.price = price;
        this.date = date;
        this.time = time;
        this.address = address;
    }

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
