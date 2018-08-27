package com.nicbit.traquer.stryker.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateInventoryRequestModel {

    @SerializedName("items")
    @Expose
    private List<UpdateItemModel> items = new ArrayList<>();

    /**
     * @return The items
     */
    public List<UpdateItemModel> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<UpdateItemModel> items) {
        this.items = items;
    }
}
