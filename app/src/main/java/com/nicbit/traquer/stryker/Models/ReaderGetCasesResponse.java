package com.nicbit.traquer.stryker.Models;

public class ReaderGetCasesResponse {

    private String caseId;
    private String id;
    private String h1;
    private String l1;
    private String l2;
    private String l3;
    private String l4;
    private String color;
    private String h2;
    private Integer count;
    private Integer isWatched;
    private Integer isException;
    private Integer isReported;
    private String h3;
    private Integer isCompleted;
    private Integer caseStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Integer getIsWatched() {
        return isWatched;
    }

    public void setIsWatched(Integer isWatched) {
        this.isWatched = isWatched;
    }

    public Integer getIsException() {
        return isException;
    }

    public void setIsException(Integer isException) {
        this.isException = isException;
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
     * @return The color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
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
     * @return The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(Integer count) {
        this.count = count;
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
     * @return The h3
     */
    public String getH3() {
        return h3;
    }


    public void setH3(String h3) {
        this.h3 = h3;
    }

}