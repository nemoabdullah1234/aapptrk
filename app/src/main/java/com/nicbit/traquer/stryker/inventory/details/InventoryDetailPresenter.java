package com.nicbit.traquer.stryker.inventory.details;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.InventoryDetailListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class InventoryDetailPresenter implements InventoryDetailContract.UserActionsListener, InventoryDetailListener {

    private final InventoryDetailContract.View mView;

    public InventoryDetailPresenter(InventoryDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getItemInventory(String skuId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setInventoryDetailListener(this);
        apiHandler.getInventoryDetails(skuId);
    }

    @Override
    public void getItemInventoryByUid(String uid) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setInventoryDetailListener(this);
        apiHandler.getInventoryDetailsByUid(uid);
    }

    @Override
    public void updateItemInventory(UpdateInventoryRequestModel updateInventoryRequestModel) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setInventoryDetailListener(this);
        apiHandler.updateItemDetails(updateInventoryRequestModel);

    }

    @Override
    public void onInventoryDetailResponse(ApiResponseModel response, NicbitException e) {
        mView.onItemInventoryResponse(response, e);
    }

    @Override
    public void onInventoryUpdate(ApiResponseModel response, NicbitException e) {
        mView.onItemUpdated(response, e);
    }
}