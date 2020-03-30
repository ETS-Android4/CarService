package com.badawy.carservice.models;

public class NavOrdersModel {
    private int productImage;
    private String productName, productPartNumber,productPrice, orderNumber,orderDate,orderTime;

    public NavOrdersModel(int productImage, String productName, String productPartNumber, String productPrice, String orderNumber, String orderDate, String orderTime) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPartNumber = productPartNumber;
        this.productPrice = productPrice;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPartNumber() {
        return productPartNumber;
    }

    public void setProductPartNumber(String productPartNumber) {
        this.productPartNumber = productPartNumber;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}


