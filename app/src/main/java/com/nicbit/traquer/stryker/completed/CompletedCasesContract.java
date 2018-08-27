package com.nicbit.traquer.stryker.completed;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface CompletedCasesContract {

    interface View {
        void onCaseHistoryResponse(ApiResponseModel response, NicbitException e);

        void onCaseHistoryFlagUpdated(ApiResponseModel response, NicbitException e);

    }

    interface UserActionsListener {

        void getCaseHistory();

        void updateCaseHistoryFlag(int isWatch, String caseNo);

    }
}
