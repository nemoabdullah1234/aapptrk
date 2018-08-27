package com.nicbit.traquer.common.issueDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReportIssueRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.ReportShippingIssueResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class ReportIssuePresenter implements ReportIssueContract.UserActionsListener, ReportShippingIssueResponseListener {
    private final ReportIssueContract.View mView;

    public ReportIssuePresenter(ReportIssueContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void reportShippingIssue(ReportIssueRequest reportIssueRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setReportShippingIssueResponseListener(this);
        apiHandler.reportShippingIssue(reportIssueRequest);
    }

    @Override
    public void reportItemComment(ReportIssueRequest reportIssueRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setReportShippingIssueResponseListener(this);
        apiHandler.reportItemComment(reportIssueRequest);
    }

    @Override
    public void onReportShippingIssueResponse(ApiResponseModel response, NicbitException e) {
        mView.onReportIssueResponseReceive(response, e);
    }
}

