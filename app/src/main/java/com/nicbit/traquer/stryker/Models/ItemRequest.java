package com.nicbit.traquer.stryker.Models;

public class ItemRequest {
    private String skuId;
    private String comment;
    private String usedStatus;

    /**
     * @return The skuId
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * @param skuId The skuId
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    /**
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return The usedStatus
     */
    public String getUsedStatus() {
        return usedStatus;
    }

    /**
     * @param usedStatus The usedStatus
     */
    public void setUsedStatus(String usedStatus) {
        this.usedStatus = usedStatus;
    }

}

