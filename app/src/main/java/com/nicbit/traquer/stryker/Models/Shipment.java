package com.nicbit.traquer.stryker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Shipment implements Parcelable {


    @SerializedName("shipmentNo")
    @Expose
    private String shipmentNo;
    @SerializedName("shipmentId")
    @Expose
    private String shipmentId;
    @SerializedName("isReported")
    @Expose
    private Integer isReported;
    @SerializedName("l1")
    @Expose
    private String l1;
    @SerializedName("l4")
    @Expose
    private String l4;

    @SerializedName("l5")
    @Expose
    private String l5;

    @SerializedName("l3")
    @Expose
    private String l3;
    @SerializedName("l2")
    @Expose
    private String l2;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("shipStatus")
    @Expose
    private Integer shipStatus;
    @SerializedName("issueId")
    @Expose
    private String issueId;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<>();

    @SerializedName("issues")
    @Expose
    private List<Issue> issues = new ArrayList<>();

    @SerializedName("map")
    @Expose
    private Map map;

    /**
     * @return The shipmentNo
     */
    public String getShipmentNo() {
        return shipmentNo;
    }

    /**
     * @param shipmentNo The shipmentNo
     */
    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    /**
     * @return The isReported
     */
    public Integer getIsReported() {
        return isReported;
    }

    /**
     * @param isReported The isReported
     */
    public void setIsReported(Integer isReported) {
        this.isReported = isReported;
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
    public String getL5() {
        return l5;
    }

    /**
     * @param l5 The l4
     */
    public void setL5(String l5) {
        this.l5 = l5;
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
     * @return The color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return The shipStatus
     */
    public Integer getShipStatus() {
        return shipStatus;
    }

    /**
     * @param shipStatus The shipStatus
     */
    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
    }

    /**
     * @return The issueId
     */
    public String getIssueId() {
        return issueId;
    }

    /**
     * @param issueId The issueId
     */
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    /**
     * @return The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return The map
     */
    public Map getMap() {
        return map;
    }

    /**
     * @param map The map
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @return The issues
     */
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * @param issues The issues
     */
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }


    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    protected Shipment(Parcel in) {
        shipmentNo = in.readString();
        shipmentId = in.readString();

        isReported = in.readByte() == 0x00 ? null : in.readInt();
        l1 = in.readString();
        l4 = in.readString();
        l5 = in.readString();
        l3 = in.readString();
        l2 = in.readString();
        color = in.readString();
        shipStatus = in.readByte() == 0x00 ? null : in.readInt();
        issueId = in.readString();
        if (in.readByte() == 0x01) {
            items = new ArrayList<>();
            in.readList(items, Item.class.getClassLoader());
        } else {
            items = null;
        }
        if (in.readByte() == 0x01) {
            issues = new ArrayList<>();
            in.readList(issues, Issue.class.getClassLoader());
        } else {
            issues = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentNo);
        dest.writeString(shipmentId);
        if (isReported == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isReported);
        }
        dest.writeString(l1);
        dest.writeString(l4);
        dest.writeString(l5);
        dest.writeString(l3);
        dest.writeString(l2);
        dest.writeString(color);
        if (shipStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shipStatus);
        }
        dest.writeString(l2);
        if (items == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(items);
        }
        if (issues == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(issues);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Shipment> CREATOR = new Parcelable.Creator<Shipment>() {
        @Override
        public Shipment createFromParcel(Parcel in) {
            return new Shipment(in);
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
        }
    };
}