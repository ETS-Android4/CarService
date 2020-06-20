package com.badawy.carservice.models;

import java.io.Serializable;

public class ServiceTypeModel implements Serializable {
    private String serviceName;
    private int price;

    public ServiceTypeModel(String serviceName, int price) {
        this.serviceName = serviceName;
        this.price = price;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
