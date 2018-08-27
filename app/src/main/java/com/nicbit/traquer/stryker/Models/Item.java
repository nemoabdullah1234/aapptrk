package com.nicbit.traquer.stryker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.nicbit.traquer.stryker.Models.inventory.SensorData;

public class Item implements Parcelable {

    private String skuId;
    private String itemId;
    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private Integer l5;
    private Integer isCompleted;
    private boolean isFound = false;
    SensorData sensor;
    int isMissing;

    public boolean isFound() {
        return isFound;
    }

    public int getIsMissing() {
        return isMissing;
    }

    public void setIsMissing(int isMissing) {
        this.isMissing = isMissing;
    }

    public boolean getFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public SensorData getSensor() {
        return sensor;
    }

    public void setSensor(SensorData sensor) {
        this.sensor = sensor;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
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
     * @return The itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId The itemId
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    /**
     * @return The l1
     */
    public String getL1() {
        return l1;
    }

    /**
     * @param l1 The l1
     */
    public void setL1(String l1) {
        this.l1 = l1;
    }

    /**
     * @return The l2
     */
    public String getL2() {
        return l2;
    }

    /**
     * @param l2 The l2
     */
    public void setL2(String l2) {
        this.l2 = l2;
    }

    /**
     * @return The l3
     */
    public String getL3() {
        return l3;
    }

    /**
     * @param l3 The l3
     */
    public void setL3(String l3) {
        this.l3 = l3;
    }

    /**
     * @return The l4
     */
    public String getL4() {
        return l4;
    }

    /**
     * @param l4 The l4
     */
    public void setL4(String l4) {
        this.l4 = l4;
    }

    /**
     * @return The l5
     */
    public Integer getL5() {
        return l5;
    }

    /**
     * @param l5 The l5
     */
    public void setL5(Integer l5) {
        this.l5 = l5;
    }

    protected Item(Parcel in) {
        skuId = in.readString();
        itemId = in.readString();
        l1 = in.readString();
        l2 = in.readString();
        l3 = in.readString();
        l4 = in.readString();
        l5 = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(skuId);
        dest.writeString(itemId);
        dest.writeString(l1);
        dest.writeString(l2);
        dest.writeString(l3);
        dest.writeString(l4);
        dest.writeInt(l5);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getSkuIdAsInt() {
        return Integer.parseInt(skuId);
    }
}