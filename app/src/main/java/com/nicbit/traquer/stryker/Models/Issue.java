package com.nicbit.traquer.stryker.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Issue implements Parcelable {

    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private Integer issueId;
    private String color;
    private Integer status;

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

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    protected Issue(Parcel in) {
        l1 = in.readString();
        l2 = in.readString();
        l3 = in.readString();
        l4 = in.readString();
        issueId = in.readByte() == 0x00 ? null : in.readInt();
        color = in.readString();
        status = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(l1);
        dest.writeString(l2);
        dest.writeString(l3);
        dest.writeString(l4);
        if (issueId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(issueId);
        }
        dest.writeString(color);
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
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

}