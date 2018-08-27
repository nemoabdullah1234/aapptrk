package com.nicbit.traquer.common.comments;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.PostIssueComment;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseItemCommentsContract {
    interface View {

        void onItemCommentsResponseReceive(ApiResponseModel response, NicbitException e);

        void onPostCommentResponseReceive(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {

        void postItemComment(PostIssueComment postIssueComment);

        void getItemComments(String caseNo, String skuId);
    }
}
