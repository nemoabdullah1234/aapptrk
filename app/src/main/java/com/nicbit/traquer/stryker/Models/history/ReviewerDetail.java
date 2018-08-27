package com.nicbit.traquer.stryker.Models.history;

import java.io.File;

public class ReviewerDetail {
    String reviewerFirstName = null;
    String reviewerLastName = null;
    String reviewerMobile = null;
    String caseNo = null;
    String reviewerCountryCode = null;
    File signatureFile;


    public File getSignatureFile() {
        return signatureFile;
    }

    public void setSignatureFile(File signatureFile) {
        this.signatureFile = signatureFile;
    }

    public String getReviewerCountryCode() {
        return reviewerCountryCode;
    }

    public void setReviewerCountryCode(String reviewerCountryCode) {
        this.reviewerCountryCode = reviewerCountryCode;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getReviewerFirstName() {
        return reviewerFirstName;
    }

    public void setReviewerFirstName(String reviewerFirstName) {
        this.reviewerFirstName = reviewerFirstName;
    }

    public String getReviewerLastName() {
        return reviewerLastName;
    }

    public void setReviewerLastName(String reviewerLastName) {
        this.reviewerLastName = reviewerLastName;
    }

    public String getReviewerMobile() {
        return reviewerMobile;
    }

    public void setReviewerMobile(String reviewerMobile) {
        this.reviewerMobile = reviewerMobile;
    }

}
