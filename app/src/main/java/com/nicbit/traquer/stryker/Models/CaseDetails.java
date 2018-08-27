package com.nicbit.traquer.stryker.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class CaseDetails implements Parcelable {

    //    private Integer isSubmitted;
    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private String l5;
    private String l6;
    private String l7;
    private String l8;
    private String surgeryDate;
    private Integer isAssigned;
    private String caseId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public Integer getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(Integer isAssigned) {
        this.isAssigned = isAssigned;
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
    public String getL5() {
        return l5;
    }

    /**
     * @param l5 The l5
     */
    public void setL5(String l5) {
        this.l5 = l5;
    }

    /**
     * @return The l6
     */
    public String getL6() {
        return l6;
    }

    /**
     * @param l6 The l6
     */
    public void setL6(String l6) {
        this.l6 = l6;
    }


    /**
     * @return The l7
     */
    public String getL7() {
        return l7;
    }

    /**
     * @param l7 The l7
     */
    public void setL7(String l7) {
        this.l7 = l7;
    }

    /**
     * @return The l8
     */
    public String getL8() {
        return l8;
    }

    /**
     * @param l8 The l8
     */
    public void setL8(String l8) {
        this.l8 = l8;
    }

    /**
     * @return The surgeryDate
     */
    public String getSurgeryDate() {
        return surgeryDate;
    }

    /**
     * @param surgeryDate The surgeryDate
     */
    public void setSurgeryDate(String surgeryDate) {
        this.surgeryDate = surgeryDate;
    }


    protected CaseDetails(Parcel in) {
        l1 = in.readString();
        l2 = in.readString();
        l3 = in.readString();
        l4 = in.readString();
        l5 = in.readString();
        l6 = in.readString();
        l7 = in.readString();
        l8 = in.readString();
        surgeryDate = in.readString();

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
        dest.writeString(l5);
        dest.writeString(l6);
        dest.writeString(l7);
        dest.writeString(l8);
        dest.writeString(surgeryDate);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CaseDetails> CREATOR = new Parcelable.Creator<CaseDetails>() {
        @Override
        public CaseDetails createFromParcel(Parcel in) {
            return new CaseDetails(in);
        }

        @Override
        public CaseDetails[] newArray(int size) {
            return new CaseDetails[size];
        }
    };

}