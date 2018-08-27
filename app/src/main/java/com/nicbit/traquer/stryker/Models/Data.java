package com.nicbit.traquer.stryker.Models;

import com.nicbit.traquer.common.newInventory.response.LocationApiResponse;
import com.nicbit.traquer.stryker.Models.forgotPassword.ReaderForgotPasswordResponse;
import com.nicbit.traquer.stryker.Models.forgotPassword.ReaderResetPasswordResponse;
import com.nicbit.traquer.stryker.Models.forgotPassword.ReaderValidateForgotPasswordResponse;
import com.nicbit.traquer.stryker.Models.history.ReaderGetCaseHistoryDetailsResponse;
import com.nicbit.traquer.stryker.Models.history.ReviewerDetail;
import com.nicbit.traquer.stryker.Models.inventory.ReaderGetItemInventoryResponse;
import com.nicbit.traquer.stryker.Models.inventory.SearchNearLocationsResponse;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private ReaderSignInResponse readerSignInResponse;
    private ReaderGenerateSessionResponse readerGenerateSessionResponse;
    private ReaderGetContentPageResponse readerGetContentPageResponse;
    private ReaderGetProfileResponse readerGetProfileResponse;
    private ReaderForgotPasswordResponse readerRequestForgotPasswordResponse;
    private ReaderValidateForgotPasswordResponse readerValidateForgotPasswordResponse;
    private ReaderResetPasswordResponse readerResetPasswordResponse;
    private ArrayList<ReaderGetCasesResponse> readerGetCasesResponse;
    private ReaderGetCaseDetailsResponse readerGetCaseDetailsResponse;
    private List<ReaderGetCasesResponse> readerGetCasesHistoryResponse;
    private ReaderGetSettingsResponse readerGetSettingsResponse;
    private ReaderGetCaseIssuesResponse readerGetCaseIssuesResponse;
    private ArrayList<ReaderGetNotificationsResponse> readerGetNotificationsResponse;
    private ReaderGetIssueCommentsResponse readerGetIssueCommentsResponse;
    private List<ReaderGetCountriesResponse> readerGetCountriesResponse;
    private List<ReaderGetItemsResponse> readerGetItemsResponse;
    private List<ReaderSearchItemsResponse> readerSearchItemsResponse;
    private ReaderGetItemDetailsResponse readerGetItemDetailsResponse;
    private ReaderReportShippingIssueResponse readerReportShippingIssueResponse;
    private ReaderReportShippingIssueResponse readerPostCaseItemCommentResponse;
    private ReaderUpdateProfileResponse readerUpdateProfileResponse;
    private ReaderGetCaseHistoryDetailsResponse readerGetCaseHistoryDetailsResponse;
    private ReaderSetItemUsedStatusResponse readerSetItemUsedStatusResponse;
    private List<ReaderSearchCasesResponse> readerSearchCasesResponse = new ArrayList<>();
    private GetItemCommentsResponse readerGetCaseItemCommentsResponse;
    private TrackingConfigResponse ReaderGetTrackingConfigurations;
    private List<ReaderGetCasesResponse> readerGetCompletedCasesResponse;
    private SearchNearLocationsResponse readerqqqq;
    private ReaderGetItemInventoryResponse readerGetItemInventoryResponse;
    private ReaderGetItemInventoryResponse readerGetCaseItemQuantityResponse;
    private ReviewerDetail ReaderSubmitSurgeryReportResponse;
    private LocationApiResponse readerSearchNearLocationsResponse;


    public SearchNearLocationsResponse getReaderqqqq() {
        return readerqqqq;
    }

    public void setReaderqqqq(SearchNearLocationsResponse readerqqqq) {
        this.readerqqqq = readerqqqq;
    }

    public void setReaderSearchNearLocationsResponse(LocationApiResponse readerSearchNearLocationsResponse) {
        this.readerSearchNearLocationsResponse = readerSearchNearLocationsResponse;
    }

    public ReviewerDetail getReaderSubmitSurgeryReportResponse() {
        return ReaderSubmitSurgeryReportResponse;
    }

    public void setReaderSubmitSurgeryReportResponse(ReviewerDetail readerSubmitSurgeryReportResponse) {
        ReaderSubmitSurgeryReportResponse = readerSubmitSurgeryReportResponse;
    }

    public ReaderGetItemInventoryResponse getReaderGetCaseItemQuantityResponse() {
        return readerGetCaseItemQuantityResponse;
    }

    public void setReaderGetCaseItemQuantityResponse(ReaderGetItemInventoryResponse readerGetCaseItemQuantityResponse) {
        this.readerGetCaseItemQuantityResponse = readerGetCaseItemQuantityResponse;
    }

    public ReaderGetItemInventoryResponse getReaderGetItemInventoryResponse() {
        return readerGetItemInventoryResponse;
    }

    public void setReaderGetItemInventoryResponse(ReaderGetItemInventoryResponse readerGetItemInventoryResponse) {
        this.readerGetItemInventoryResponse = readerGetItemInventoryResponse;
    }

    public LocationApiResponse getReaderSearchNearLocationsResponse() {
        return readerSearchNearLocationsResponse;
    }

    public void setReaderSearchNearLocationsResponse(SearchNearLocationsResponse readerSearchNearLocationsResponse) {
        this.readerqqqq = readerSearchNearLocationsResponse;
    }

    public ReaderReportShippingIssueResponse getReaderPostCaseItemCommentResponse() {
        return readerPostCaseItemCommentResponse;
    }

    public void setReaderPostCaseItemCommentResponse(ReaderReportShippingIssueResponse readerPostCaseItemCommentResponse) {
        this.readerPostCaseItemCommentResponse = readerPostCaseItemCommentResponse;
    }

    public TrackingConfigResponse getReaderGetTrackingConfigurations() {
        return ReaderGetTrackingConfigurations;
    }

    public void setReaderGetTrackingConfigurations(TrackingConfigResponse readerGetTrackingConfigurations) {
        ReaderGetTrackingConfigurations = readerGetTrackingConfigurations;
    }

    public GetItemCommentsResponse getReaderGetCaseItemCommentsResponse() {
        return readerGetCaseItemCommentsResponse;
    }

    public void setReaderGetCaseItemCommentsResponse(GetItemCommentsResponse readerGetCaseItemCommentsResponse) {
        this.readerGetCaseItemCommentsResponse = readerGetCaseItemCommentsResponse;
    }

    public List<ReaderGetCasesResponse> getReaderGetCompletedCasesResponse() {
        return readerGetCompletedCasesResponse;
    }

    public void setReaderGetCompletedCasesResponse(List<ReaderGetCasesResponse> readerGetCompletedCasesResponse) {
        this.readerGetCompletedCasesResponse = readerGetCompletedCasesResponse;
    }

    public GetItemCommentsResponse getGetItemCommentsResponse() {
        return readerGetCaseItemCommentsResponse;
    }

    public void setGetItemCommentsResponse(GetItemCommentsResponse getItemCommentsResponse) {
        this.readerGetCaseItemCommentsResponse = getItemCommentsResponse;
    }

    /**
     * @return The readerSearchCasesResponse
     */
    public List<ReaderSearchCasesResponse> getReaderSearchCasesResponse() {
        return readerSearchCasesResponse;
    }

    /**
     * @param readerSearchCasesResponse The readerSearchCasesResponse
     */
    public void setReaderSearchCasesResponse(List<ReaderSearchCasesResponse> readerSearchCasesResponse) {
        this.readerSearchCasesResponse = readerSearchCasesResponse;
    }

    /**
     * @return The readerSetItemUsedStatusResponse
     */
    public ReaderSetItemUsedStatusResponse getReaderSetItemUsedStatusResponse() {
        return readerSetItemUsedStatusResponse;
    }

    /**
     * @param readerSetItemUsedStatusResponse The ReaderSetItemUsedStatusResponse
     */
    public void setReaderSetItemUsedStatusResponse(ReaderSetItemUsedStatusResponse readerSetItemUsedStatusResponse) {
        this.readerSetItemUsedStatusResponse = readerSetItemUsedStatusResponse;
    }

    /**
     * @return The readerGetCaseHistoryDetailsResponse
     */
    public ReaderGetCaseHistoryDetailsResponse getReaderGetCaseHistoryDetailsResponse() {
        return readerGetCaseHistoryDetailsResponse;
    }

    /**
     * @param readerGetCaseHistoryDetailsResponse The readerGetCaseHistoryDetailsResponse
     */
    public void setReaderGetCaseHistoryDetailsResponse(ReaderGetCaseHistoryDetailsResponse readerGetCaseHistoryDetailsResponse) {
        this.readerGetCaseHistoryDetailsResponse = readerGetCaseHistoryDetailsResponse;
    }

    /**
     * @return The readerUpdateProfileResponse
     */
    public ReaderUpdateProfileResponse getReaderUpdateProfileResponse() {
        return readerUpdateProfileResponse;
    }

    /**
     * @param readerUpdateProfileResponse The readerUpdateProfileResponse
     */
    public void setReaderUpdateProfileResponse(ReaderUpdateProfileResponse readerUpdateProfileResponse) {
        this.readerUpdateProfileResponse = readerUpdateProfileResponse;
    }

    /**
     * @return The readerReportShippingIssueResponse
     */
    public ReaderReportShippingIssueResponse getReaderReportShippingIssueResponse() {
        return readerReportShippingIssueResponse;
    }

    /**
     * @param readerReportShippingIssueResponse The readerReportShippingIssueResponse
     */
    public void setReaderReportShippingIssueResponse(ReaderReportShippingIssueResponse readerReportShippingIssueResponse) {
        this.readerReportShippingIssueResponse = readerReportShippingIssueResponse;
    }

    /**
     * @return The readerGetItemDetailsResponse
     */
    public ReaderGetItemDetailsResponse getReaderGetItemDetailsResponse() {
        return readerGetItemDetailsResponse;
    }

    /**
     * @param readerGetItemDetailsResponse The readerGetItemDetailsResponse
     */
    public void setReaderGetItemDetailsResponse(ReaderGetItemDetailsResponse readerGetItemDetailsResponse) {
        this.readerGetItemDetailsResponse = readerGetItemDetailsResponse;
    }

    /**
     * @return The readerGetCountriesResponse
     */
    public List<ReaderGetCountriesResponse> getReaderGetCountriesResponse() {
        return readerGetCountriesResponse;
    }

    /**
     * @param readerGetCountriesResponse The readerGetCountriesResponse
     */
    public void setReaderGetCountriesResponse(List<ReaderGetCountriesResponse> readerGetCountriesResponse) {
        this.readerGetCountriesResponse = readerGetCountriesResponse;
    }


    /**
     * @return The readerSearchItemsResponse
     */
    public List<ReaderSearchItemsResponse> getReaderSearchItemsResponse() {
        return readerSearchItemsResponse;
    }

    /**
     * @param readerSearchItemsResponse The readerSearchItemsResponse
     */
    public void setReaderSearchItemsResponse(List<ReaderSearchItemsResponse> readerSearchItemsResponse) {
        this.readerSearchItemsResponse = readerSearchItemsResponse;
    }

    /**
     * @return The readerGetItemsResponse
     */
    public List<ReaderGetItemsResponse> getReaderGetItemsResponse() {
        return readerGetItemsResponse;
    }

    /**
     * @param readerGetItemsResponse The readerGetItemsResponse
     */
    public void setReaderGetItemsResponse(List<ReaderGetItemsResponse> readerGetItemsResponse) {
        this.readerGetItemsResponse = readerGetItemsResponse;
    }

    public ArrayList<ReaderGetNotificationsResponse> getReaderGetNotificationsResponse() {
        return readerGetNotificationsResponse;
    }

    public void setReaderGetNotificationsResponse(ArrayList<ReaderGetNotificationsResponse> readerGetNotificationsResponse) {
        this.readerGetNotificationsResponse = readerGetNotificationsResponse;
    }

    /**
     * @return The readerGetIssueCommentsResponse
     */
    public ReaderGetIssueCommentsResponse getReaderGetIssueCommentsResponse() {
        return readerGetIssueCommentsResponse;
    }

    /**
     * @param readerGetIssueCommentsResponse The readerGetIssueCommentsResponse
     */
    public void setReaderGetIssueCommentsResponse(ReaderGetIssueCommentsResponse readerGetIssueCommentsResponse) {
        this.readerGetIssueCommentsResponse = readerGetIssueCommentsResponse;
    }

    /**
     * @return The readerGetCaseIssuesResponse
     */
    public ReaderGetCaseIssuesResponse getReaderGetCaseIssuesResponse() {
        return readerGetCaseIssuesResponse;
    }

    /**
     * @param readerGetCaseIssuesResponse The readerGetCaseIssuesResponse
     */
    public void setReaderGetCaseIssuesResponse(ReaderGetCaseIssuesResponse readerGetCaseIssuesResponse) {
        this.readerGetCaseIssuesResponse = readerGetCaseIssuesResponse;
    }


//

    /**
     * @return The readerGetSettingsResponse
     */
    public ReaderGetSettingsResponse getReaderGetSettingsResponse() {
        return readerGetSettingsResponse;
    }

    /**
     * @param readerGetSettingsResponse The readerGetSettingsResponse
     */
    public void setReaderGetSettingsResponse(ReaderGetSettingsResponse readerGetSettingsResponse) {
        this.readerGetSettingsResponse = readerGetSettingsResponse;
    }


    /**
     * @return The readerGetCasesHistoryResponse
     */
    public List<ReaderGetCasesResponse> getReaderGetCasesHistoryResponse() {
        return readerGetCasesHistoryResponse;
    }

    /**
     * @param readerGetCasesHistoryResponse The readerGetCasesHistoryResponse
     */

    public void setReaderGetCasesHistoryResponse(List<ReaderGetCasesResponse> readerGetCasesHistoryResponse) {
        this.readerGetCasesHistoryResponse = readerGetCasesHistoryResponse;
    }


    public ReaderGetProfileResponse getReaderGetProfileResponse() {
        return readerGetProfileResponse;
    }

    public void setReaderGetProfileResponse(ReaderGetProfileResponse readerGetProfileResponse) {
        this.readerGetProfileResponse = readerGetProfileResponse;
    }

    /**
     * @return The readerGetCaseDetailsResponse
     */
    public ReaderGetCaseDetailsResponse getReaderGetCaseDetailsResponse() {
        return readerGetCaseDetailsResponse;
    }

    /**
     * @param readerGetCaseDetailsResponse The readerGetCaseDetailsResponse
     */
    public void setReaderGetCaseDetailsResponse(ReaderGetCaseDetailsResponse readerGetCaseDetailsResponse) {
        this.readerGetCaseDetailsResponse = readerGetCaseDetailsResponse;
    }

    public ReaderSignInResponse getReaderSignInResponse() {
        return readerSignInResponse;
    }

    public void setReaderSignInResponse(ReaderSignInResponse readerSignInResponse) {
        this.readerSignInResponse = readerSignInResponse;
    }

    public ReaderGenerateSessionResponse getReaderGenerateSessionResponse() {
        return readerGenerateSessionResponse;
    }

    public void setReaderGenerateSessionResponse(ReaderGenerateSessionResponse readerGenerateSessionResponse) {
        this.readerGenerateSessionResponse = readerGenerateSessionResponse;
    }

    /**
     * @return The readerGetContentPageResponse
     */
    public ReaderGetContentPageResponse getReaderGetContentPageResponse() {
        return readerGetContentPageResponse;
    }

    /**
     * @param readerGetContentPageResponse The readerGetContentPageResponse
     */
    public void setReaderGetContentPageResponse(ReaderGetContentPageResponse readerGetContentPageResponse) {
        this.readerGetContentPageResponse = readerGetContentPageResponse;
    }


    /**
     * @return The readerGetCasesResponse
     */
    public ArrayList<ReaderGetCasesResponse> getReaderGetCasesResponse() {
        return readerGetCasesResponse;
    }

    /**
     * @param readerGetCasesResponse The readerGetCasesResponse
     */
    public void setReaderGetCasesResponse(ArrayList<ReaderGetCasesResponse> readerGetCasesResponse) {
        this.readerGetCasesResponse = readerGetCasesResponse;
    }

    public ReaderForgotPasswordResponse getReaderRequestForgotPasswordResponse() {
        return readerRequestForgotPasswordResponse;
    }

    public void setReaderRequestForgotPasswordResponse(ReaderForgotPasswordResponse readerRequestForgotPasswordResponse) {
        this.readerRequestForgotPasswordResponse = readerRequestForgotPasswordResponse;
    }

    public ReaderValidateForgotPasswordResponse getReaderValidateForgotPasswordResponse() {
        return readerValidateForgotPasswordResponse;
    }

    public void setReaderValidateForgotPasswordResponse(ReaderValidateForgotPasswordResponse readerValidateForgotPasswordResponse) {
        this.readerValidateForgotPasswordResponse = readerValidateForgotPasswordResponse;
    }

    public ReaderResetPasswordResponse getReaderResetPasswordResponse() {
        return readerResetPasswordResponse;
    }

    public void setReaderResetPasswordResponse(ReaderResetPasswordResponse readerResetPasswordResponse) {
        this.readerResetPasswordResponse = readerResetPasswordResponse;
    }
}