package com.nicbit.traquer.common.issueDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.PostIssueComment;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.PostCommentResponseListener;
import com.nicbit.traquer.stryker.listener.ReportIssueCommentsResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class ReportIssueDetailPresenter implements ReportIssueDetailContract.UserActionsListener, PostCommentResponseListener, ReportIssueCommentsResponseListener {
    private final ReportIssueDetailContract.View mView;


    public ReportIssueDetailPresenter(ReportIssueDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void postIssueComment(PostIssueComment postIssueComment) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setPostCommentResposeListener(this);
        apiHandler.postIssueComment(postIssueComment);
    }

    @Override
    public void getIssueComments(String caseNo, String shippingNo, String issueId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setReportIssueCommentsResponseListener(this);
        apiHandler.getIssueComments(caseNo, shippingNo, issueId);
    }

    @Override
    public void onPostIssueCommentResponse(ApiResponseModel response, NicbitException e) {
        mView.onPostCommentResponseReceive(response, e);
    }

    @Override
    public void onIssueCommentsResponse(ApiResponseModel response, NicbitException e) {
        mView.onIssueCommentsResponseReceive(response, e);
    }
}
