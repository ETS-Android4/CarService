package com.badawy.carservice.models;

import java.util.List;

public class SparePartsCategoryModel {
    private  String partsCategoryName;
    private List<String> partIdList;

    public SparePartsCategoryModel(String partsCategoryName, List<String> partIdList) {
        this.partsCategoryName = partsCategoryName;
        this.partIdList = partIdList;
    }

    public String getPartsCategoryName() {
        return partsCategoryName;
    }

    public void setPartsCategoryName(String partsCategoryName) {
        this.partsCategoryName = partsCategoryName;
    }

    public List<String> getPartIdList() {
        return partIdList;
    }

    public void setPartIdList(List<String> partIdList) {
        this.partIdList = partIdList;
    }
}
