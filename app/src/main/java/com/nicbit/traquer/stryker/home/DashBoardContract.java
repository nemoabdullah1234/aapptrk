package com.nicbit.traquer.stryker.home;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ReaderGetCasesResponse;
import com.nicbit.traquer.stryker.exception.NicbitException;

import java.util.List;

public interface DashBoardContract {

    interface View {
        void onGetCasesDone(ApiResponseModel response, NicbitException e);

        void CasesFromCacheReceived(List<ReaderGetCasesResponse> caseList);

        void onCaseFlagUpdated(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void getCases();

        void updateCaseFlag(int isWatch, String caseNo);
    }
}
