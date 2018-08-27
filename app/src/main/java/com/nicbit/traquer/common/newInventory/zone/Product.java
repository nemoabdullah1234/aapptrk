package com.nicbit.traquer.common.newInventory.zone;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class Product implements Parcelable {


    int isFound = 0;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("things")
    @Expose
    private List<ZoneApiResponse.Thing> things = null;

    public int getIsFound() {
        return isFound;
    }

    public void setIsFound(int isFound) {
        this.isFound = isFound;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<ZoneApiResponse.Thing> getThings() {
        return things;
    }

    public void setThings(List<ZoneApiResponse.Thing> things) {
        this.things = things;
    }


    protected Product(Parcel in) {
        isFound = in.readInt();
        id = in.readString();
        code = in.readString();
        name = in.readString();
        if (in.readByte() == 0x01) {
            things = new ArrayList<ZoneApiResponse.Thing>();
            in.readList(things, ZoneApiResponse.Thing.class.getClassLoader());
        } else {
            things = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isFound);
        dest.writeString(id);
        dest.writeString(code);
        dest.writeString(name);
        if (things == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(things);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}