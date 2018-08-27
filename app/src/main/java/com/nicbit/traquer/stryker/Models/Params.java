package com.nicbit.traquer.stryker.Models;

public class Params {
    private String caseId;
    private String id;
    private String skuId;
    private Integer notificationType;
    private String shippingNo;
    private String issueId;
    private Integer serviceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return The notificationType
     */
    public Integer getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType The notificationType
     */
    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

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
}
