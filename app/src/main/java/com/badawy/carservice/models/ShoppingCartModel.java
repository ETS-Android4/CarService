package com.badawy.carservice.models;

public class ShoppingCartModel {
    private int partImage;
    private String partName;
    private String partNumber;
    private String partPrice;
    private int partQuantity;

    public ShoppingCartModel(int partImage, String partName, String partNumber, String partPrice, int partQuantity) {
        this.partImage = partImage;
        this.partName = partName;
        this.partNumber = partNumber;
        this.partPrice = partPrice;
        this.partQuantity = partQuantity;
    }


    public int getPartImage() {
        return partImage;
    }

    public void setPartImage(int partImage) {
        this.partImage = partImage;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(String partPrice) {
        this.partPrice = partPrice;
    }

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }
}

