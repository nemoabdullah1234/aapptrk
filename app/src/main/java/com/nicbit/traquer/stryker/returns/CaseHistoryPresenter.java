package com.nicbit.traquer.stryker.returns;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateCaseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseHistoryResponseListener;
import com.nicbit.traquer.stryker.listener.UpdateCaseFlagListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class CaseHistoryPresenter implements CaseHistoryContract.UserActionsListener, CaseHistoryResponseListener, UpdateCaseFlagListener {
    private final CaseHistoryContract.View mCaseHistoryView;

    public CaseHistoryPresenter(CaseHistoryContract.View mCaseHistoryView) {
        this.mCaseHistoryView = mCaseHistoryView;
    }

    @Override
    public void onCaseHistoryResponse(ApiResponseModel response, NicbitException e) {
        mCaseHistoryView.onCaseHistoryResponse(response, e);
    }

    @Override
    public void getCaseHistory(String sortBy, String sortOrder) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseHistoryResponseListener(this);
        apiHandler.getCasesHistory(sortBy, sortOrder);
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
