package com.nicbit.traquer.stryker.Models;

import com.nicbit.traquer.common.cloudinary.CloudinaryImage;

import java.util.ArrayList;
import java.util.List;

public class ReportIssueRequest {

    private String caseNo;
    private String shippingNo;
    private String comment;
    private String skuId;
    private String skuIds;
    private List<CloudinaryImage> images = new ArrayList<>();


    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }

    /**
     * @return The caseNo
     */
    public String getCaseNo() {
        return caseNo;
    }

    /**
     * @param caseNo The caseNo
     */
    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    /**
     * @return The shippingNo
     */
    public String getShippingNo() {
        return shippingNo;
    }

    /**
     * @param shippingNo The shippingNo
     */
    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }


    /**
     * @return The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return The images
     */
    public List<CloudinaryImage> getImages() {
        return images;
    }

    /**
     * @param images The images
     */
    public void setImages(List<CloudinaryImage> images) {
        this.images = images;
    }

}