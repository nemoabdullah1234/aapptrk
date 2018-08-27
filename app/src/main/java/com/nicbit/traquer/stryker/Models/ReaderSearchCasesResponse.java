package com.nicbit.traquer.stryker.Models;

public class ReaderSearchCasesResponse {
    private Integer isCaseAssociated;
    private String caseId;
    private String surgeryDate;
    private Integer isReported;
    private String h1;
    private String h2;
    private String h3;
    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private Integer type;
    private Integer caseStatus;
    private Params params;


    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }

    /**
     * @return The isCaseAssociated
     */
    public Integer getIsCaseAssociated() {
        return isCaseAssociated;
    }

    /**
     * @param isCaseAssociated The isCaseAssociated
     */
    public void setIsCaseAssociated(Integer isCaseAssociated) {
        this.isCaseAssociated = isCaseAssociated;
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
     * @return The h1
     */
    public String getH1() {
        return h1;
    }

    /**
     * @param h1 The h1
     */
    public void setH1(String h1) {
        this.h1 = h1;
    }

    /**
     * @return The h2
     */
    public String getH2() {
        return h2;
    }

    /**
     * @param h2 The h2
     */
    public void setH2(String h2) {
        this.h2 = h2;
    }

    /**
     * @return The h3
     */
    public String getH3() {
        return h3;
    }

    /**
     * @param h3 The h3
     */
    public void setH3(String h3) {
        this.h3 = h3;
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
     * @return The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return The params
     */
    public Params getParams() {
        return params;
    }

    /**
     * @param params The params
     */
    public void setParams(Params params) {
        this.params = params;
    }


}
