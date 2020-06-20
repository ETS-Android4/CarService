package com.badawy.carservice.models;

public class ShoppingCartModel {
    private SparePartModel sparePartModel;
    private int partQuantity;
    private String oldPrice;

    public ShoppingCartModel() {
    }

    public SparePartModel getSparePartModel() {
        return sparePartModel;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setSparePartModel(SparePartModel sparePartModel) {
        this.sparePartModel = sparePartModel;
    }

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }
}

