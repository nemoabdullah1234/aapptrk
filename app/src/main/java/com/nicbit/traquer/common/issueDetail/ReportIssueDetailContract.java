package com.nicbit.traquer.common.issueDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.PostIssueComment;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface ReportIssueDetailContract {
    interface View {

        void onIssueCommentsResponseReceive(ApiResponseModel response, NicbitException e);

        void onPostCommentResponseReceive(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {

        void postIssueComment(PostIssueComment postIssueComment);

        void getIssueComments(String caseNo, String shippingNo, String issueId);
    }
}
