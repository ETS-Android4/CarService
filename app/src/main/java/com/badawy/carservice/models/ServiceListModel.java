package com.badawy.carservice.models;

import android.widget.Button;

public class ServiceListModel {
    private int serviceIcon;
    private String serviceLabel;
    private String serviceDescription;
    private Button serviceListButton;
    private String serviceButtonLabel;


    public ServiceListModel(int serviceIcon, String serviceLabel, String serviceDescription, Button serviceListButton, String serviceButtonLabel) {
        this.serviceIcon = serviceIcon;
        this.serviceLabel = serviceLabel;
        this.serviceDescription = serviceDescription;
        this.serviceListButton = serviceListButton;
        this.serviceButtonLabel = serviceButtonLabel;
    }


    public int getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(int serviceIcon) {
        this.serviceIcon = serviceIcon;
    }

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Button getServiceListButton() {
        return serviceListButton;
    }

    public void setServiceListButton(Button serviceListButton) {
        this.serviceListButton = serviceListButton;
    }

    public String getServiceButtonLabel() {
        return serviceButtonLabel;
    }

    public void setServiceButtonLabel(String serviceButtonLabel) {
        this.serviceButtonLabel = serviceButtonLabel;
    }
}
