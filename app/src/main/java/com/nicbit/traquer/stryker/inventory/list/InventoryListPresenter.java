package com.nicbit.traquer.stryker.inventory.list;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.inventory.UpdateInventoryScanRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.InventoryListListener;
import com.nicbit.traquer.stryker.network.ApiHandler;

public class InventoryListPresenter implements InventoryListContract.UserActionsListener, InventoryListListener {
    private final InventoryListContract.View mInventoryView;

    public InventoryListPresenter(InventoryListContract.View mInventoryView) {
        this.mInventoryView = mInventoryView;
    }

    @Override
    public void searchNearLocations(double lat, double lang, Integer locationId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setInventoryListListener(this);
        apiHandler.SearchNearLocation(lat, lang, locationId);
    }

    @Override
    public void updateInventoryScan(UpdateInventoryScanRequest updateInventoryRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setInventoryListListener(this);
        apiHandler.updateInventoryScanData(updateInventoryRequest);
    }

    @Override
    public void onSearchLocationResponse(ApiResponseModel response, NicbitException e) {
        mInventoryView.onSearchNearLocationsResponse(response, e);
    }

    @Override
    public void onInventoryScanDataUpdated(ApiResponseModel response, NicbitException e) {
        mInventoryView.onInventoryScanUpdated(response, e);

    }
}
