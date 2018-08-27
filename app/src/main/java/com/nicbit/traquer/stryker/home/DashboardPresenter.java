package com.nicbit.traquer.stryker.home;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateCaseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CasesResponseListener;
import com.nicbit.traquer.stryker.listener.UpdateCaseFlagListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class DashboardPresenter implements DashBoardContract.UserActionsListener, CasesResponseListener, UpdateCaseFlagListener {
    private DashBoardContract.View mCaseView;

    public DashboardPresenter(DashBoardContract.View mCaseView) {
        this.mCaseView = mCaseView;
    }

    @Override
    public void onCasesResponse(ApiResponseModel response, NicbitException e) {
        mCaseView.onGetCasesDone(response, e);
    }


    @Override
    public void getCases() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCasesResponseListener(this);
        apiHandler.getCases();
    }

    @Override
    public void updateCaseFlag(int isWatch, String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateCaseFlagListener(this);
        UpdateCaseModel updateCaseModel = new UpdateCaseModel();
        updateCaseModel.setIsWatch(isWatch);
        updateCaseModel.setCaseNo(caseNo);
        apiHandler.updateCaseFlag(updateCaseModel);
    }


    @Override
    public void onUpdateCaseFlagResponse(ApiResponseModel response, NicbitException e) {
        mCaseView.onCaseFlagUpdated(response, e);
    }
}
