package com.nicbit.traquer.stryker.Models;

import java.util.ArrayList;
import java.util.List;

public class ItemUsedRequest {
    private String caseNo;
    private List<ItemRequest> items = new ArrayList<>();
    private Integer isSubmitted;

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
     * @return The items
     */
    public List<ItemRequest> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }

    /**
     * @return The isSubmitted
     */
    public Integer getIsSubmitted() {
        return isSubmitted;
    }

    /**
     * @param isSubmitted The isSubmitted
     */
    public void setIsSubmitted(Integer isSubmitted) {
        this.isSubmitted = isSubmitted;
    }


}