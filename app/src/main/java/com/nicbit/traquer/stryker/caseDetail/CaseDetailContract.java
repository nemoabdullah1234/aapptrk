package com.nicbit.traquer.stryker.caseDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseDetailContract {
    interface View {
        void onGetCaseDetailDone(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void getCaseDetail(String token, String caseNo);
    }
}
