package com.nicbit.traquer.stryker.search;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface SearchContract {
    interface View {
        void onSearchCasesResponse(ApiResponseModel response, NicbitException e);

        void onFlagUpdated(ApiResponseModel response, NicbitException e);

    }

    interface UserActionsListener {
        void updateFlag(int isWatch, String caseNo);

        void searchCases(String query);
    }
}
