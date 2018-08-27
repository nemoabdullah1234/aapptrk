package com.nicbit.traquer.stryker.returns.detail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ItemUsedRequest;
import com.nicbit.traquer.stryker.Models.history.ReviewerDetail;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseHistoryDetailContract {
    interface View {
        void onCaseHistoryDetailResponse(ApiResponseModel response, NicbitException e);

        void onItemUsedStatusResponse(ApiResponseModel response, NicbitException e);

        void onSurgeryReportResponse(ApiResponseModel response, NicbitException e);

    }

    interface UserActionsListener {

        void getCaseHistoryDetail(String caseNo);

        void setItemUsedStatus(ItemUsedRequest request);

        void submitSurgeryReport(ReviewerDetail reviewerDetail);

    }
}
