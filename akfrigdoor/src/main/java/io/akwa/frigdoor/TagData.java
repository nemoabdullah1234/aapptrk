package io.akwa.frigdoor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class TagData implements Parcelable {
    public String chipNo;
    public String companyName;
    public String batchNo;
    public String mfgDate;
    public String expDate;
    public String serialNo;
    public String requestID;
    public String clientID;
    public String uniqueID;
    public String randomNo;
    public String algoType;
    public String productName;
    public BluTagInfo bluTagInfo=new BluTagInfo();
    public int offlineStatus;
    public String offlineCause;
    public String sku;
    public List<Attribute> attribute=new ArrayList<>();
    public int issueStatus;
    public String issueTo;
    public String hospital;

    public TagData(){

    }

    protected TagData(Parcel in) {
        chipNo = in.readString();
        batchNo = in.readString();
        mfgDate = in.readString();
        expDate = in.readString();
        serialNo = in.readString();
        requestID = in.readString();
        clientID = in.readString();
        uniqueID = in.readString();
        randomNo = in.readString();
        algoType = in.readString();
        productName = in.readString();
        bluTagInfo = (BluTagInfo) in.readValue(BluTagInfo.class.getClassLoader());
        offlineStatus = in.readInt();
        offlineCause = in.readString();
        sku = in.readString();
        if (in.readByte() == 0x01) {
            attribute = new ArrayList<Attribute>();
            in.readList(attribute, Attribute.class.getClassLoader());
        } else {
            attribute = null;
        }
        issueStatus = in.readInt();
        issueTo = in.readString();
        hospital = in.readString();
        companyName=in.readString();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chipNo);
        dest.writeString(batchNo);
        dest.writeString(mfgDate);
        dest.writeString(expDate);
        dest.writeString(serialNo);
        dest.writeString(requestID);
        dest.writeString(clientID);
        dest.writeString(uniqueID);
        dest.writeString(randomNo);
        dest.writeString(algoType);
        dest.writeString(productName);
        dest.writeValue(bluTagInfo);
        dest.writeInt(offlineStatus);
        dest.writeString(offlineCause);
        dest.writeString(sku);
        if (attribute == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(attribute);
        }
        dest.writeInt(issueStatus);
        dest.writeString(issueTo);
        dest.writeString(hospital);
        dest.writeString(companyName);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TagData> CREATOR = new Parcelable.Creator<TagData>() {
        @Override
        public TagData createFromParcel(Parcel in) {
            return new TagData(in);
        }

        @Override
        public TagData[] newArray(int size) {
            return new TagData[size];
        }
    };

}
