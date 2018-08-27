package com.nicbit.traquer.stryker.caseDetail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseDetailResponseListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class CaseDetailPresenter implements CaseDetailContract.UserActionsListener, CaseDetailResponseListener {

    private final CaseDetailContract.View mCaseView;

    public CaseDetailPresenter(CaseDetailContract.View mCaseView) {
        this.mCaseView = mCaseView;
    }

    @Override
    public void getCaseDetail(String token, String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseDetailResponseListener(this);
        apiHandler.getCaseDetails(token, caseNo);
    }

    @Override
    public void onCaseDetailResponse(ApiResponseModel response, NicbitException e) {
        mCaseView.onGetCaseDetailDone(response, e);
    }
}
