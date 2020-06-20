package com.badawy.carservice.models;

import java.util.ArrayList;

public class SparePartModel {
    private String manufacturerImage;
    private String productImage;
    private String productName;
    private String productID;
    private String productDescription;
    private String productPrice;
    private ArrayList<SparePartDetailsModel> productDetailsList;

    public SparePartModel(String manufacturerImage, String productImage, String productName, String productID, String productDescription, String productPrice, ArrayList<SparePartDetailsModel> productDetailsList) {
        this.manufacturerImage = manufacturerImage;
        this.productImage = productImage;
        this.productName = productName;
        this.productID = productID;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productDetailsList = productDetailsList;
    }

    public SparePartModel() {
    }

    public String getManufacturerImage() {
        return manufacturerImage;
    }

    public void setManufacturerImage(String manufacturerImage) {
        this.manufacturerImage = manufacturerImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID.toUpperCase().trim();
    }

    public void setProductID(String productID) {
        this.productID = productID.toUpperCase().trim();
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<SparePartDetailsModel> getProductDetailsList() {
        return productDetailsList;
    }

    public void setProductDetailsList(ArrayList<SparePartDetailsModel> productDetailsList) {
        this.productDetailsList = productDetailsList;
    }
}

