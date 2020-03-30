package com.badawy.carservice.models;

public class SelectCarModel {
    private byte carId;
    private int carImage;
    private String carName;

    public SelectCarModel(byte carId, int carImage, String carName) {
        this.carId = carId;
        this.carImage = carImage;
        this.carName = carName;
    }

    public byte getCarId() {
        return carId;
    }

    public void setCarId(byte carId) {
        this.carId = carId;
    }

    public int getCarImage() {
        return carImage;
    }

    public void setCarImage(int carImage) {
        this.carImage = carImage;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}
