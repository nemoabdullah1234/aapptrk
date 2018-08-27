package com.nicbit.traquer.stryker.listener;

import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.exception.NicbitException;

public interface SaveGcmListener {
    void onSaveGcmResponse(ApiResponseModel response, NicbitException e);

}
