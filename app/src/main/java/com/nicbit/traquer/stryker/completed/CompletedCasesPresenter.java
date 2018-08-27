package com.nicbit.traquer.stryker.completed;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateCaseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseHistoryResponseListener;
import com.nicbit.traquer.stryker.listener.UpdateCaseFlagListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class CompletedCasesPresenter implements CompletedCasesContract.UserActionsListener, CaseHistoryResponseListener, UpdateCaseFlagListener {
    private final CompletedCasesContract.View mCaseHistoryView;

    public CompletedCasesPresenter(CompletedCasesContract.View mCaseHistoryView) {
        this.mCaseHistoryView = mCaseHistoryView;
    }

    @Override
    public void onCaseHistoryResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onCaseHistoryResponse(response, e);
    }

    @Override
    public void getCaseHistory() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseHistoryResponseListener(this);
        apiHandler.getCompletedCases();
    }

    @Override
    public void updateCaseHistoryFlag(int isWatch, String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateCaseFlagListener(this);
        UpdateCaseModel updateCaseModel = new UpdateCaseModel();
        updateCaseModel.setIsWatch(isWatch);
        updateCaseModel.setCaseNo(caseNo);
        apiHandler.updateCaseFlag(updateCaseModel);
    }

    @Override
    public void onUpdateCaseFlagResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onCaseHistoryFlagUpdated(response, e);

    }
}
