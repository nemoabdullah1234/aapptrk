package com.nicbit.traquer.stryker.search;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateCaseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.ApiListener;
import com.nicbit.traquer.stryker.listener.UpdateCaseFlagListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class SearchPresenter implements SearchContract.UserActionsListener, ApiListener, UpdateCaseFlagListener {
    private final SearchContract.View mSearchPresenter;

    public SearchPresenter(SearchContract.View mSearchPresenter) {
        this.mSearchPresenter = mSearchPresenter;
    }

    @Override
    public void updateFlag(int isWatch, String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateCaseFlagListener(this);
        UpdateCaseModel updateCaseModel = new UpdateCaseModel();
        updateCaseModel.setIsWatch(isWatch);
        updateCaseModel.setCaseNo(caseNo);
        apiHandler.updateCaseFlag(updateCaseModel);
    }

    @Override
    public void searchCases(String query) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setApiListener(this);
        apiHandler.searchCases(query);
    }

    @Override
    public void onApiResponse(ApiResponseModel response, NicbitException e) {
        mSearchPresenter.onSearchCasesResponse(response, e);
    }

    @Override
    public void onUpdateCaseFlagResponse(ApiResponseModel response, NicbitException e) {
        mSearchPresenter.onFlagUpdated(response, e);
    }
}
