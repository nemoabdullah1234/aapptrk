package com.nicbit.traquer.stryker.Models;

import java.util.ArrayList;
import java.util.List;

public class ReaderGetItemDetailsResponse {

    private String caseId;
    private String skuId;
    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private String l5;
    private List<Attribute> attributes = new ArrayList<>();

    /**
     * @return The caseId
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     * @param caseId The caseId
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId;
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
     * @return The attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes The attributes
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }


}