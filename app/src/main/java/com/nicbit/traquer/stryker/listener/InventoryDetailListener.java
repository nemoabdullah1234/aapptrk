package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface InventoryDetailListener {
    void onInventoryDetailResponse(ApiResponseModel response, NicbitException e);

    void onInventoryUpdate(ApiResponseModel response, NicbitException e);

}
