package com.nicbit.traquer.stryker.Models.inventory;

import com.nicbit.traquer.stryker.Models.ProductDetails;

import java.util.ArrayList;

public class ReaderGetItemInventoryResponse {

    private ProductDetails productDetails;

    ArrayList<Item> items;

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
