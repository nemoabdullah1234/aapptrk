package com.nicbit.traquer.common.comments;


import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.PostIssueComment;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseItemCommentsResponseListener;
import com.nicbit.traquer.stryker.listener.PostCommentResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class CaseItemCommentsPresenter implements CaseItemCommentsContract.UserActionsListener, PostCommentResponseListener, CaseItemCommentsResponseListener {
    private final CaseItemCommentsContract.View mView;


    public CaseItemCommentsPresenter(CaseItemCommentsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void postItemComment(PostIssueComment postIssueComment) {

    }

    @Override
    public void getItemComments(String caseNo, String skuId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseItemCommentsResponseListener(this);
        apiHandler.getItemComments(caseNo, skuId);
    }

    @Override
    public void onPostIssueCommentResponse(ApiResponseModel response, NicbitException e) {
        mView.onPostCommentResponseReceive(response, e);
    }

    @Override
    public void onItemCommentsResponse(ApiResponseModel response, NicbitException e) {
        mView.onItemCommentsResponseReceive(response, e);
    }
}
