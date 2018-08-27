package com.nicbit.traquer.stryker.returns.detail;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.CaseItemQuantityListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class ItemQuantityPresenter implements ItemQuantityContract.UserActionsListener, CaseItemQuantityListener {

    private final ItemQuantityContract.View mView;

    public ItemQuantityPresenter(ItemQuantityContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getCaseItemQuantity(Integer skuId, String caseNo) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseItemQuantityListener(this);
        apiHandler.getCaseItemQuantity(skuId, caseNo);
    }

    @Override
    public void updateCaseItemQuantity(UpdateInventoryRequestModel updateInventoryRequestModel) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setCaseItemQuantityListener(this);
        apiHandler.updateCaseItemQuantity(updateInventoryRequestModel);
    }

    @Override
    public void onCaseItemQuantityUpdated(ApiResponseModel response, NicbitException e) {
        mView.onCaseItemQuantityUpdated(response, e);
    }

    @Override
    public void onCaseItemQuantityReceive(ApiResponseModel response, NicbitException e) {
        mView.onCaseItemQuantityReceive(response, e);
    }
}