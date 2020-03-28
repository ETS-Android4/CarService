package com.badawy.carservice.models;

public class ProductItemModel {
    private int manufacturerImage;
    private int productImage;
    private String productName;
    private String productPartNumber;
    private String productDescription;
    private String productPrice;

    public ProductItemModel(int manufacturerImage, int productImage, String productName, String productPartNumber, String productDescription, String productPrice) {
        this.manufacturerImage = manufacturerImage;
        this.productImage = productImage;
        this.productName = productName;
        this.productPartNumber = productPartNumber;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public int getManufacturerImage() {
        return manufacturerImage;
    }

    public void setManufacturerImage(int manufacturerImage) {
        this.manufacturerImage = manufacturerImage;
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
}
