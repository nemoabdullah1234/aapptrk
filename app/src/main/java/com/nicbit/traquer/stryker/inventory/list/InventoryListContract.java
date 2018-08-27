package com.nicbit.traquer.stryker.inventory.list;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.inventory.UpdateInventoryScanRequest;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface InventoryListContract {

    interface View {
        void onSearchNearLocationsResponse(ApiResponseModel response, NicbitException e);

        void onInventoryScanUpdated(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void searchNearLocations(double lat, double lang, Integer locationId);

        void updateInventoryScan(UpdateInventoryScanRequest updateInventoryRequest);
    }
}
