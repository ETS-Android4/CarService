package com.badawy.carservice.models;

public class CarModel {
    private String carID ;
    private String carYear;
    private String carBrand;
    private String carModel;

    public CarModel(String carID, String carYear, String carBrand, String carModel) {
        this.carID = carID;
        this.carYear = carYear;
        this.carBrand = carBrand;
        this.carModel = carModel;
    }

    public CarModel() {
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
