package com.nicbit.traquer.stryker.listener;


import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface InventoryListListener {
    void onSearchLocationResponse(ApiResponseModel response, NicbitException e);

    void onInventoryScanDataUpdated(ApiResponseModel response, NicbitException e);

}
