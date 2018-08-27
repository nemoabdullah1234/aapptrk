package com.nicbit.traquer.stryker.Models;

public class ReaderGetItemsResponse {

    private Integer itemId;
    private String itemName;

    /**
     * @return The itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId The itemId
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * @return The itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName The itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}