package com.nicbit.traquer.stryker.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateItemModel {

    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("skuId")
    @Expose
    private String skuId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    private Integer usedQuantity;
    private String caseNo;


    public Integer getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(Integer usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    /**
     * @return The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId The productId
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

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
     * @return The quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity The quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
