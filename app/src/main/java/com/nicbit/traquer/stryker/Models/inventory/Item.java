package com.nicbit.traquer.stryker.Models.inventory;

public class Item {

    String productId;
    String skuId;
    String code;
    String name;
    int quantity;
    int updatedQuantity;
    int usedQuantity;
    boolean isChange;


    public int getUpdatedQuantity() {
        return updatedQuantity;
    }

    public void setUpdatedQuantity(int updatedQuantity) {
        this.updatedQuantity = updatedQuantity;
    }

    public void setUsedQuantity(int usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public int getUsedQuantity() {
        return usedQuantity;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
