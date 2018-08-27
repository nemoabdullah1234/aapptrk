package com.nicbit.traquer.stryker.Models.inventory;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    int productId;
    int skuId;
    String code;
    String name;
    int isFound = 0;
    SensorData sensor;


    protected Product(Parcel in) {
        productId = in.readInt();
        skuId = in.readInt();
        code = in.readString();
        name = in.readString();
        isFound = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeInt(skuId);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeInt(isFound);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getIsFound() {
        return isFound;
    }

    public void setIsFound(int isFound) {
        this.isFound = isFound;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public SensorData getSensor() {
        return sensor;
    }

    public void setSensor(SensorData sensor) {
        this.sensor = sensor;
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
}
