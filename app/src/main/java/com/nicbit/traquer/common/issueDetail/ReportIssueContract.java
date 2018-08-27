package com.nicbit.traquer.common.issueDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReportIssueRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface ReportIssueContract {
    interface View {

        void onReportIssueResponseReceive(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {

        void reportShippingIssue(ReportIssueRequest reportIssueRequest);

        void reportItemComment(ReportIssueRequest reportIssueRequest);
    }
}
