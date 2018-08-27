package com.nicbit.traquer.stryker.returns.detail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ItemUsedRequest;
import com.nicbit.traquer.stryker.Models.history.ReviewerDetail;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseHistoryDetailResponseListener;
import com.nicbit.traquer.stryker.listener.ItemUsedStatusResponseListener;
import com.nicbit.traquer.stryker.listener.SurgeryReportSubmitResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;


public class CaseHistoryDetailPresenter implements CaseHistoryDetailContract.UserActionsListener, CaseHistoryDetailResponseListener, ItemUsedStatusResponseListener, SurgeryReportSubmitResponseListener {

    private final CaseHistoryDetailContract.View mCaseHistoryView;

    public CaseHistoryDetailPresenter(CaseHistoryDetailContract.View mCaseHistoryView) {
        this.mCaseHistoryView = mCaseHistoryView;
    }

    @Override
    public void onCaseHistoryDetailResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onCaseHistoryDetailResponse(response, e);
    }

    @Override
    public void getCaseHistoryDetail(String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseHistoryDetailResponseListener(this);
        apiHandler.getCaseHistoryDetails(caseNo);
    }

    @Override
    public void setItemUsedStatus(ItemUsedRequest request) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setItemUsedStatusResponseListener(this);
        apiHandler.setItemUsedStatus(request);
    }

    @Override
    public void submitSurgeryReport(ReviewerDetail reviewerDetail) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setSurgeryReportSubmitResponseListener(this);
        apiHandler.submitSurgeryReport(reviewerDetail);
    }

    @Override
    public void onItemUsedStatusResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onItemUsedStatusResponse(response, e);
    }

    @Override
    public void onSurgeryReportSubmitResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onSurgeryReportResponse(response, e);
    }
}
