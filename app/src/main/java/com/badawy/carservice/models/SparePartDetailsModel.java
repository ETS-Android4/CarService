package com.badawy.carservice.models;

public class SparePartDetailsModel {

    private String detailName;
    private String detailValue;


    public SparePartDetailsModel(String detailName, String detailValue) {
        this.detailName = detailName;
        this.detailValue = detailValue;
    }

    public SparePartDetailsModel() {
    }


    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailValue() {
        return detailValue;
    }

    public void setDetailValue(String detailValue) {
        this.detailValue = detailValue;
    }
}
