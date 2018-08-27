package com.nicbit.traquer.stryker.returns;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CaseHistoryContract {

    interface View {
        void onCaseHistoryResponse(ApiResponseModel response, NicbitException e);

        void onCaseHistoryFlagUpdated(ApiResponseModel response, NicbitException e);

    }

    interface UserActionsListener {

        void getCaseHistory(String sortBy, String sortOrder);

        void updateCaseHistoryFlag(int isWatch, String caseNo);

    }
}
