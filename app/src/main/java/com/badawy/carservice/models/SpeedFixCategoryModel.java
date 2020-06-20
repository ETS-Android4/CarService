package com.badawy.carservice.models;

public class SpeedFixCategoryModel {
    private int categoryImage;
    private String categoryName;
    private int categoryPrice;

    public SpeedFixCategoryModel(int categoryImage, String categoryName, int categoryPrice) {
        this.categoryImage = categoryImage;
        this.categoryName = categoryName;
        this.categoryPrice = categoryPrice;
    }



    public int getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(int categoryPrice) {
        this.categoryPrice = categoryPrice;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
