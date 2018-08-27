package com.nicbit.traquer.stryker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ReaderGetCaseIssuesResponse implements Parcelable {
    private CaseDetails caseDetails;
    private List<Shipment> shipments = new ArrayList<>();


    /**
     * @return The caseDetails
     */
    public CaseDetails getCaseDetails() {
        return caseDetails;
    }

    /**
     * @param caseDetails The caseDetails
     */
    public void setCaseDetails(CaseDetails caseDetails) {
        this.caseDetails = caseDetails;
    }

    /**
     * @return The shipments
     */
    public List<Shipment> getShipments() {
        return shipments;
    }

    /**
     * @param shipments The shipments
     */
    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    protected ReaderGetCaseIssuesResponse(Parcel in) {
        caseDetails = (CaseDetails) in.readValue(CaseDetails.class.getClassLoader());
        if (in.readByte() == 0x01) {
            shipments = new ArrayList<>();
            in.readList(shipments, Shipment.class.getClassLoader());
        } else {
            shipments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(caseDetails);
        if (shipments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(shipments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReaderGetCaseIssuesResponse> CREATOR = new Parcelable.Creator<ReaderGetCaseIssuesResponse>() {
        @Override
        public ReaderGetCaseIssuesResponse createFromParcel(Parcel in) {
            return new ReaderGetCaseIssuesResponse(in);
        }

        @Override
        public ReaderGetCaseIssuesResponse[] newArray(int size) {
            return new ReaderGetCaseIssuesResponse[size];
        }
    };
}
