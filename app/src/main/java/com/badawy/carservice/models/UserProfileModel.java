package com.badawy.carservice.models;

import java.io.Serializable;

public class UserProfileModel implements Serializable {
    private String userId;
    private String userName;
    private String emailAddress;
    private String phoneNumber;
    private String Address;
    private String profileImageUri;
    public String getUserId() {
        return userId;
    }

    public UserProfileModel() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }




    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
