package com.nicbit.traquer.stryker.Models.history;

import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Item;

import java.util.ArrayList;
import java.util.List;

public class ReaderGetCaseHistoryDetailsResponse {

    private CaseDetails caseDetails;
    private List<Item> items = new ArrayList<>();
    private ReviewerDetail reviewerDetails;

    public ReviewerDetail getReviewerDetails() {
        return reviewerDetails;
    }

    public void setReviewerDetails(ReviewerDetail reviewerDetails) {
        this.reviewerDetails = reviewerDetails;
    }

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
     * @return The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }


}