package com.badawy.carservice.models;

import java.util.ArrayList;

public class OrderModel {
    private UserProfileModel userProfileObject;
    private ArrayList<ShoppingCartModel> productList;
    private String totalPrice;
    private String orderID;

    public OrderModel() {
    }

    public UserProfileModel getUserProfileObject() {
        return userProfileObject;
    }

    public void setUserProfileObject(UserProfileModel userProfileObject) {
        this.userProfileObject = userProfileObject;
    }

    public ArrayList<ShoppingCartModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ShoppingCartModel> productList) {
        this.productList = productList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
